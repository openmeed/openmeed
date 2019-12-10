package me.ebenezergraham.honours.platform.listener;

import me.ebenezergraham.honours.platform.model.Payload;
import me.ebenezergraham.honours.platform.services.RewardEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import static me.ebenezergraham.honours.platform.util.Constants.CLOSED_PULL_REQUEST;
import static me.ebenezergraham.honours.platform.util.Constants.OPENED_PULL_REQUEST;

@Component
public class PullRequestListener {
    private final Logger logger = LoggerFactory.getLogger(PullRequestListener.class);
    private final RewardEngine rewardEngine;

    public PullRequestListener(final RewardEngine rewardEngine) {
        this.rewardEngine = rewardEngine;
    }

    @JmsListener(destination = CLOSED_PULL_REQUEST)
    public void closedPrListener( Payload message){
        logger.info("Processing {} event from GitHub", message.getAction());
        rewardEngine.processClosedPullRequest(message);
    }

    @JmsListener(destination = OPENED_PULL_REQUEST)
    public void openedPrlistener( Payload message){
        logger.info("Processing {} event from GitHub", message.getAction());
        rewardEngine.processOpenedPullRequest(message);
    }
}
