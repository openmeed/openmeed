package me.ebenezergraham.honours.platform.listener;

import me.ebenezergraham.honours.platform.model.Payload;
import me.ebenezergraham.honours.platform.model.Reward;
import me.ebenezergraham.honours.platform.repository.AllocatedIssueRepository;
import me.ebenezergraham.honours.platform.repository.RewardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static me.ebenezergraham.honours.platform.util.Constants.ISSUE_EVENT_ASSIGNED_ACTION_SELECTOR;

@Component
public class IssueConsumer {
  private final Logger logger = LoggerFactory.getLogger(IssueConsumer.class);
  private final AllocatedIssueRepository allocatedIssueRepository;
  private final RewardRepository rewardRepository;

  public IssueConsumer(final AllocatedIssueRepository allocatedIssueRepository,
                       final RewardRepository rewardRepository) {
    this.allocatedIssueRepository = allocatedIssueRepository;
    this.rewardRepository = rewardRepository;
  }

  @JmsListener(destination = ISSUE_EVENT_ASSIGNED_ACTION_SELECTOR)
  public void assignedIssueListener(Payload message) {
    logger.info("Processing Issue event {}", message.getIssue().getHtmlUrl());
    Optional<Reward> reward = rewardRepository.findRewardByIssueId(message.getIssue().getUrl());
    reward.ifPresent(value -> {
      logger.info("Saving issue in to rewards {}", message.getAction());
      allocatedIssueRepository.save(message.getIssue());
    });
  }
}
