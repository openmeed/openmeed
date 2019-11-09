package me.ebenezergraham.honours.platform.listener;

import me.ebenezergraham.honours.platform.model.Payload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@EnableJms
public class MergeListener {
    private final Logger logger = LoggerFactory.getLogger(MergeListener.class);

    @JmsListener(destination = "merge-queue")
    public void listener(Payload message){

        logger.info("Message received {} ", message.getAction());
    }
}
