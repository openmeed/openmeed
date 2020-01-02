package me.ebenezergraham.honours.platform.services;

import com.google.common.collect.Lists;
import me.ebenezergraham.honours.platform.interfaces.IRewardEngine;
import me.ebenezergraham.honours.platform.model.*;
import me.ebenezergraham.honours.platform.repository.AllocatedIssueRepository;
import me.ebenezergraham.honours.platform.repository.RewardRepository;
import me.ebenezergraham.honours.platform.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static me.ebenezergraham.honours.platform.util.Constants.EMAIL_EVENT_CLAIMED_ACTION_SELECTOR;

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
  private Logger logger = LoggerFactory.getLogger(RewardEngine.class.getName());

  public RewardEngine(UserRepository userRepository,
                      RewardRepository rewardRepository,
                      AllocatedIssueRepository allocatedIssueRepository,
                      JmsTemplate jmsTemplate) {

    this.allocatedIssueRepository = allocatedIssueRepository;
    this.userRepository = userRepository;
    this.rewardRepository = rewardRepository;
    this.jmsTemplate = jmsTemplate;
    this.validationCriteria = new HashMap<>();
    this.validationCriteria.put("BENEFACTORS_MUST_BE_PR_REVIEWERS", false);
    this.validationCriteria.put("CONTRIBUTOR_MUST_BE_PR_ASSIGNEE", false);
    this.validationCriteria.put("BENEFACTORS_CANNOT_REDEEM_REWARD", false);
  }

  @Override
  public void process(Payload payload) {
    logger.info("Processing claim to {}", payload.getPull_request().getIssue_url());
    // Check if successfully merged
    if (payload.getPull_request().isMerged()) {
      // Then retrieve the reward linked to the issue this request solves
      Optional<Reward> result = rewardRepository.findRewardByUrl(payload.getPull_request().getIssue_url());
      // If an incentive exists for it then proceed to validate
      result.ifPresent(reward -> {
        // If the payload satisfies the criteria to transfer incentive
        if (validate(payload, reward)) {
          // Fetch the user who has to receive this
          Optional<User> user = userRepository.findByUsername(payload.getSender().getLogin());
          logger.info("Updating recipient of reward");
          reward.getReceipients().add(payload.getSender().getLogin());
          rewardRepository.save(reward);
          user.ifPresent(value -> {
            value.setPoints(Integer.parseInt(result.get().getValue()));
            logger.info("Awarding user {} with incentive {}", user.get().getName(), reward.getValue());
            userRepository.save(value);
            Map<String, String> notificationDetails = new HashMap<>();
            notificationDetails.put("EMAIL", user.get().getEmail());
            notificationDetails.put("PR_TITLE", payload.getPull_request().getTitle());
            notificationDetails.put("ISSUE_URL", payload.getPull_request().getHtml_url());
            notificationDetails.put("REWARD", reward.getValue());
            notificationDetails.put("NAME", user.get().getName());
            jmsTemplate.convertAndSend(EMAIL_EVENT_CLAIMED_ACTION_SELECTOR, notificationDetails);
            rewardRepository.delete(result.get());
          });
        }
      });
    } else {
      logger.info("Rejected Pull Request");
    }
  }

  @Override
  public void validationCriteria(Map<String, Boolean> criteria) {
    this.validationCriteria = criteria;
  }

  @Override
  public boolean validate(Payload payload, Reward reward) {
    logger.info("validating PR associated to issue: {}", payload.getPull_request().getIssue_url());
    try {
      if (validationCriteria.get("BENEFACTORS_MUST_BE_PR_REVIEWERS")) {
        // Verify that the necessary authorities have reviewed submission
        ArrayList<String> requestedReviewers = Lists.newArrayList(payload.getPull_request().getRequested_reviewers());
        for (String authority : reward.getAuthorizer()) {
          if (!requestedReviewers.contains(authority) || requestedReviewers.size() == 0) return false;
        }
      }
      //Verify that contributor assigned the issue is the same entity that sent the solution
      if (validationCriteria.get("CONTRIBUTOR_MUST_BE_PR_ASSIGNEE")) {
        // Verify contributor was assigned the responsibility to resolve issue
        Optional<Issue> issue = allocatedIssueRepository.findIssueByHtmlUrl(payload.getPull_request().getIssue_url());
        if (issue.isPresent()) {
          if (!payload.getSender().getLogin().equals(issue.get().getAssigneeName())) return false;
        }
      }
      //Verify that the maintainer who assigned the reward is not redeeming it.
      if (validationCriteria.get("BENEFACTORS_CANNOT_REDEEM_REWARD")) {
        for (GitHubUser assignee : payload.getPull_request().getAssignees()) {
          if (reward.getAuthorizer().contains(assignee.getLogin())) return false;
        }
      }
    } catch (Exception e) {
      logger.error("Error during validation{}", e.getLocalizedMessage());
      return false;
    }
    return true;
  }
}
