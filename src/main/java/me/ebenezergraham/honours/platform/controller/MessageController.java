package me.ebenezergraham.honours.platform.controller;

import me.ebenezergraham.honours.platform.model.Payload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;

import javax.jms.Queue;

import static me.ebenezergraham.honours.platform.util.Constants.PULL_REQUEST;

@RestController
@RequestMapping("/api/v1/")
public class MessageController {

  private Queue queue;
  private JmsTemplate jmsTemplate;

  public MessageController(Queue queue, JmsTemplate jmsTemplate) {
    this.queue = queue;
    this.jmsTemplate = jmsTemplate;
  }

  private final Logger logger = LoggerFactory.getLogger(MessageController.class);

  @PostMapping("github/events")
  public ResponseEntity<String> publish(@RequestBody final Payload payload) {

    switch (payload.getAction()) {
      case "opened":
        logger.info("Sending Opened Message", payload.getPull_request().toString());
        jmsTemplate.convertAndSend(PULL_REQUEST, payload);
        break;
      default:
        return new ResponseEntity(HttpStatus.NOT_MODIFIED);
    }
    return new ResponseEntity(HttpStatus.OK);
  }

  public void processPayload(Payload payload){
    if(payload.getPull_request().getState().equals("closed")){

    }

  }
}
