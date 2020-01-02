package me.ebenezergraham.honours.platform.listener;

import me.ebenezergraham.honours.platform.model.Payload;
import me.ebenezergraham.honours.platform.model.Reward;
import me.ebenezergraham.honours.platform.repository.RewardRepository;
import me.ebenezergraham.honours.platform.services.RewardEngine;
import me.ebenezergraham.honours.platform.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

import static me.ebenezergraham.honours.platform.util.Constants.PULL_REQUEST_EVENT_CLOSED_ACTION_SELECTOR;
import static me.ebenezergraham.honours.platform.util.Constants.PULL_REQUEST_EVENT_EDITED_ACTION_SELECTOR;
import static me.ebenezergraham.honours.platform.util.Constants.PULL_REQUEST_REVIEW_EVENT_APPROVED_SELECTOR;

@Component
public class PullRequestConsumer {
  private final Logger logger = LoggerFactory.getLogger(PullRequestConsumer.class);
  private final RewardEngine rewardEngine;
  private final String[] reservedWords;
  private final RewardRepository rewardRepository;

  public PullRequestConsumer(final RewardEngine rewardEngine, RewardRepository rewardRepository) {
    this.rewardEngine = rewardEngine;
    this.rewardRepository = rewardRepository;
    this.reservedWords = new String[]{"close", "closes", "closed", "fix", "fixes", "fixed", "resolve", "resolves", "resolved"};
  }

  @JmsListener(destination = PULL_REQUEST_EVENT_CLOSED_ACTION_SELECTOR)
  public void closedPrListener(Payload message) {
    logger.info("Processing {} event from GitHub", message.getAction());
    rewardEngine.process(message);
  }

  @JmsListener(destination = PULL_REQUEST_REVIEW_EVENT_APPROVED_SELECTOR)
  public void pullRequestApproved(Payload message) {
    logger.info("Processing {} pull request event from GitHub", message.getAction());
    rewardEngine.process(message);
  }


  @JmsListener(destination = PULL_REQUEST_EVENT_EDITED_ACTION_SELECTOR)
  public void editedPrListener(Payload message) {
    logger.info("Processing {} event from GitHub", message.getAction());
    String body = message.getPull_request().getBody();
    if (StringUtil.containReservedWord(body, reservedWords)) {
      Arrays.stream(body.split(" ")).parallel().forEach(entry -> {
        if (entry.contains("#")) {
          String issueId = entry.substring(1).trim();
          String newURL = message.getPull_request().getIssue_url();
          int lastIndexOf = newURL.lastIndexOf('/');
          StringBuffer sb = new StringBuffer(newURL);
          sb.delete(lastIndexOf + 1, sb.length());
          sb.append(issueId);
          newURL = sb.toString();
          Optional<Reward> reward = rewardRepository.findRewardByUrl(newURL);
          reward.ifPresent(value -> {
            if (value.getAuthorizer().stream().parallel().anyMatch(message.getSender().getLogin()::contains)) {
              value.setUrl(message.getPull_request().getIssue_url());
            }
            rewardRepository.save(value);
          });
        }
      });
    }
  }
}
