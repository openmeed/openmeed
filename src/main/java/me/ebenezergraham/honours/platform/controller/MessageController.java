package me.ebenezergraham.honours.platform.controller;

import me.ebenezergraham.honours.platform.listener.MergeListener;
import me.ebenezergraham.honours.platform.model.Payload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;

import javax.jms.Queue;

import static me.ebenezergraham.honours.platform.util.Constants.PULL_REQUEST;

@RestController
@RequestMapping("/api/v1/")
public class MessageController {

    @Autowired
    private Queue queue;
    @Autowired
    private JmsTemplate jmsTemplate;

    private final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @PostMapping("github/events")
    public ResponseEntity<String> publish(@RequestBody final Payload payload) {
        logger.info(payload.toString());

        jmsTemplate.convertAndSend(PULL_REQUEST, payload);
        return new ResponseEntity(HttpStatus.OK);
    }
}
