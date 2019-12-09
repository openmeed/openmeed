package me.ebenezergraham.honours.platform.listener;

import me.ebenezergraham.honours.platform.model.Payload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import static me.ebenezergraham.honours.platform.util.Constants.PULL_REQUEST;
import static me.ebenezergraham.honours.platform.util.Constants.SELECTOR_PULL_REQUEST;

@Component
public class PullRequestListener {
    private final Logger logger = LoggerFactory.getLogger(PullRequestListener.class);

    @JmsListener(destination = PULL_REQUEST)
    public void listener( Payload message){
        System.out.println("......................................Test");
        logger.info("Message received {} ", message.getAction());
    }
}
