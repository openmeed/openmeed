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

import static me.ebenezergraham.honours.platform.util.Constants.ASSIGNED_EVENT;
import static me.ebenezergraham.honours.platform.util.Constants.ISSUE_EVENT_TYPE;

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

  @JmsListener(destination = ISSUE_EVENT_TYPE)
  public void assignedIssueListener(Payload message) {
    logger.info("Processing Issue event {}", message.getIssue().getUrl());

    switch (message.getAction()) {
      case ASSIGNED_EVENT:
        Optional<Reward> reward = rewardRepository.findRewardByIssueId(message.getIssue().getHtml_url());
        reward.ifPresent(value->{
          logger.info("Saving issue in to issues {}", message.getAction());
          allocatedIssueRepository.save(message.getIssue());
        });
        break;
      default:
        logger.info("Action {} not supported yet", message.getAction());
    }

  }
}
