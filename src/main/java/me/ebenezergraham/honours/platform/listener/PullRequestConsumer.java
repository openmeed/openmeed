package me.ebenezergraham.honours.platform.listener;

import me.ebenezergraham.honours.platform.model.Payload;
import me.ebenezergraham.honours.platform.services.RewardEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import static me.ebenezergraham.honours.platform.util.Constants.*;

@Component
public class PullRequestConsumer {
  private final Logger logger = LoggerFactory.getLogger(PullRequestConsumer.class);
  private final RewardEngine rewardEngine;

  public PullRequestConsumer(final RewardEngine rewardEngine) {
    this.rewardEngine = rewardEngine;
  }

  @JmsListener(destination = CLOSED_PULL_REQUEST)
  public void closedPrListener(Payload message) {
    logger.info("Processing {} event from GitHub", message.getAction());
    rewardEngine.process(message);
  }

  @JmsListener(destination = ASSIGNED_EVENT)
  public void assignedIssueListener(Payload message) {
    logger.info("Processing {} event from GitHub", message.getAction());
  }

  @JmsListener(destination = OPENED_PULL_REQUEST)
  public void openedPrlistener(Payload message) {
    logger.info("Processing {} event from GitHub", message.getAction());
    rewardEngine.process(message);
  }
}
