package me.ebenezergraham.honours.platform.listener;

import me.ebenezergraham.honours.platform.services.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Map;

import static me.ebenezergraham.honours.platform.util.Constants.EMAIL_EVENT_CLAIMED_ACTION_SELECTOR;

@Component
public class MessageConsumer {
    private final Logger logger = LoggerFactory.getLogger(MessageConsumer.class);
    private final EmailService emailService;

    public MessageConsumer(final EmailService emailService) {
        this.emailService = emailService;
    }

    @JmsListener(destination = EMAIL_EVENT_CLAIMED_ACTION_SELECTOR)
    private void notifyContributor(Map<String, String> details) {
        logger.info("Sending message");
        emailService.sendSimpleMessage(
            details.get("EMAIL"),
            details.get("PR_TITLE"),
            "Congratulations " + details.get("NAME") +
                ",\n\nYou have been award the prize for issue: " + details.get("ISSUE_URL")
                + "\n\nRegards,\nOpenMeed");
    }
}
