package me.ebenezergraham.honours.platform.services;

import com.google.common.collect.Lists;
import me.ebenezergraham.honours.platform.interfaces.IRewardEngine;
import me.ebenezergraham.honours.platform.model.Issue;
import me.ebenezergraham.honours.platform.model.Payload;
import me.ebenezergraham.honours.platform.model.Reward;
import me.ebenezergraham.honours.platform.model.User;
import me.ebenezergraham.honours.platform.repository.AllocatedIssueRepository;
import me.ebenezergraham.honours.platform.repository.RewardRepository;
import me.ebenezergraham.honours.platform.repository.UserRepository;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

import static me.ebenezergraham.honours.platform.util.Constants.CLOSED_EVENT;
import static me.ebenezergraham.honours.platform.util.Constants.OPENED_EVENT;
import static me.ebenezergraham.honours.platform.util.Constants.SEND_EMAIL;

/**
 * @author Ebenezer Graham
 * Created on 9/30/19
 */
@Service
public class RewardEngine implements IRewardEngine {

  private final AllocatedIssueRepository allocatedIssueRepository;
  private final RewardRepository rewardRepository;
  private final UserRepository userRepository;
  private final JmsTemplate jmsTemplate;
  private Map<String, Boolean> validationCriteria;


  public RewardEngine(UserRepository userRepository,
                      RewardRepository rewardRepository,
                      AllocatedIssueRepository allocatedIssueRepository,
                      JmsTemplate jmsTemplate) {

    this.allocatedIssueRepository = allocatedIssueRepository;
    this.userRepository = userRepository;
    this.rewardRepository = rewardRepository;
    this.jmsTemplate = jmsTemplate;
    this.validationCriteria = new HashMap<>();
    this.validationCriteria.put("AUTHORITIES_MUST_BE_REVIEWERS", true);
    this.validationCriteria.put("CONTRIBUTOR_MUST_BE_ASSIGNEE", true);
  }

  @Override
  public void process(Payload payload) {
    switch (payload.getAction()) {
      case CLOSED_EVENT:
        closed(payload);
        break;
      default:
        break;
    }
  }

  @Override
  public void validationCriteria(Map<String, Boolean> criteria) {
    this.validationCriteria = criteria;
  }

  @Override
  public boolean validate(Payload payload, Reward reward) {
    if (validationCriteria.get("AUTHORITIES_MUST_BE_REVIEWERS")) {
      // Verify that the necessary authorities have reviewed submission
      ArrayList<String> requestedReviewers = Lists.newArrayList(payload.getPull_request().getRequested_reviewers());
      for (String authority : reward.getAuthorizer()) {
        if (!requestedReviewers.contains(authority)) return false;
      }
    }
    if (validationCriteria.get("CONTRIBUTOR_MUST_BE_ASSIGNEE"))
      reward.getAuthorizer().contains(payload.getPull_request().getAssignee());

    // Verify contributor was assigned the responsibility to resolve issue
    //Optional<Issue> issue = allocatedIssueRepository.findIssueByUrl(payload.getPull_request().getIssue_url());
    if (!payload.getSender().getLogin().equals(reward.getIssue().getAssigneeName())) return false;
    return true;
  }


  /**
   * Check if Pull Request's issue has an issue associated
   * If a reward is present for the specific pull request's issue,
   * Reward the user who submitted the pull request
   */
  private void closed(Payload payload) {
    // Check if successfully merged
    if (payload.getPull_request().isMerged()) {
      // Then retrieve the reward linked to the issue this request solves
      Optional<Reward> result = rewardRepository.findRewardByIssueId(payload.getPull_request().getIssue_url());
      // If an incentive exists for it then proceed to validate
      result.ifPresent(reward -> {
        //If the payload satisfies the criteria to transfer incentive
          if (validate(payload, reward)) {
            //Fetch the user who has to receive this
            Optional<User> user = userRepository.findByUsername(payload.getSender().getLogin());
            user.get().setPoints(Integer.parseInt(result.get().getValue()));
            userRepository.save(user.get());
            Map<String, String> notificationDetails = new HashMap<>();
            notificationDetails.put("EMAIL", user.get().getEmail());
            notificationDetails.put("PR_TITLE", payload.getPull_request().getTitle());
            notificationDetails.put("ISSUE_URL", payload.getPull_request().getIssue_url());
            notificationDetails.put("NAME", user.get().getName());
            jmsTemplate.convertAndSend(SEND_EMAIL, notificationDetails);
            rewardRepository.delete(result.get());
          }
      });
    }
  }

}
