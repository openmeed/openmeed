package me.ebenezergraham.honours.platform.services;

import me.ebenezergraham.honours.platform.model.Issue;
import me.ebenezergraham.honours.platform.model.Payload;
import me.ebenezergraham.honours.platform.model.Reward;
import me.ebenezergraham.honours.platform.model.User;
import me.ebenezergraham.honours.platform.repository.AllocatedIssueRepository;
import me.ebenezergraham.honours.platform.repository.RewardRepository;
import me.ebenezergraham.honours.platform.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.apache.poi.hslf.record.RecordTypes.List;

/**
 * @author Ebenezer Graham
 * Created on 9/30/19
 */
@Service
public class RewardEngine {

  private final AllocatedIssueRepository allocatedIssueRepository;
  private final RewardRepository rewardRepository;
  private final UserRepository userRepository;
  private final EmailService emailService;


  public RewardEngine(UserRepository userRepository,
                      RewardRepository rewardRepository,
                      AllocatedIssueRepository allocatedIssueRepository,
                      EmailService emailService) {

    this.allocatedIssueRepository = allocatedIssueRepository;
    this.userRepository = userRepository;
    this.rewardRepository = rewardRepository;
    this.emailService = emailService;
  }

  /**
   * Check if Pull Request's issue has an issue associated
   * If a reward is present for the specific pull request's issue,
   * Reward the user who submitted the pull request
   */
  public void processClosedPullRequest(Payload payload) {
    if (payload.getPull_request().isMerged()) {
      Optional<Reward> result = rewardRepository.findRewardByIssueId(payload.getPull_request().getIssue_url());
      result.ifPresent(reward -> {
        if (reward.getIssueId().equalsIgnoreCase(payload.getPull_request().getIssue_url())) {
          Optional<User> user = userRepository.findByUsername(payload.getSender().getLogin());
          user.get().setPoints(Integer.parseInt(result.get().getValue()));
          userRepository.save(user.get());
          Map<String, String> notificationDetails = new HashMap<>();
          notificationDetails.put("EMAIL", user.get().getEmail());
          notificationDetails.put("PR_TITLE", payload.getPull_request().getTitle());
          notificationDetails.put("ISSUE_URL", payload.getPull_request().getIssue_url());
          notificationDetails.put("NAME", user.get().getName());
          notifyContributor(notificationDetails);
        }
      });

      rewardRepository.delete(result.get());
    }
  }

  public void processOpenedPullRequest(Payload payload) {
    if (!payload.getPull_request().isMerged()) {
      Optional<Issue> result = allocatedIssueRepository.findIssueByIssue(payload.getPull_request().getIssue_url());
      result.ifPresent(issue -> {
        if (issue.getContributor().equalsIgnoreCase(payload.getPull_request().getIssue_url())) {
          Optional<User> user = userRepository.findByUsername(payload.getSender().getLogin());
          Map<String, String> notificationDetails = new HashMap<>();
          notificationDetails.put("EMAIL", user.get().getEmail());
          notificationDetails.put("PR_TITLE", payload.getPull_request().getTitle());
          notificationDetails.put("ISSUE_URL", payload.getPull_request().getIssue_url());
          notificationDetails.put("NAME", user.get().getName());
          notifyContributor(notificationDetails);
        }
      });
    }
  }

  private void notifyContributor(Map<String, String> details) {
    emailService.sendSimpleMessage(
        details.get("EMAIL"),
        details.get("PR_TITLE"),
        "Congratulations " + details.get("NAME") +
            ",\n\nYou have been award the prize for issue: " + details.get("ISSUE_URL")
            + "\n\nRegards,\nOpenMeed");
  }
}
