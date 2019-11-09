package me.ebenezergraham.honours.platform.controller;

import me.ebenezergraham.honours.platform.model.Payload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;

import javax.jms.Queue;

@RestController
@RequestMapping("/api/v1/")
public class MessageController {

    @Autowired
    private Queue queue;
    @Autowired
    private JmsTemplate jmsTemplate;

    @PostMapping("github/events")
    public ResponseEntity<String> publish(@RequestBody final Payload payload) {
        payload.toString();
        jmsTemplate.convertAndSend(queue, payload);
        return new ResponseEntity(HttpStatus.OK);
    }
}
