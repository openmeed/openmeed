package me.ebenezergraham.honours.platform.controller;

import me.ebenezergraham.honours.platform.model.Payload;
import me.ebenezergraham.honours.platform.services.RewardEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;

import static me.ebenezergraham.honours.platform.util.Constants.*;

@RestController
@RequestMapping("/api/v1/")
public class EventsController {

  private JmsTemplate jmsTemplate;
  private RewardEngine rewardEngine;

  public EventsController(final RewardEngine rewardEngine,
                          final JmsTemplate jmsTemplate) {
    this.rewardEngine = rewardEngine;
    this.jmsTemplate = jmsTemplate;
  }

  private final Logger logger = LoggerFactory.getLogger(EventsController.class);

  @PostMapping("github/events")
  public ResponseEntity<String> publish(@RequestBody final Payload payload) {
    switch (payload.getAction()) {
      case ASSIGNED_EVENT:
        logger.info("Sending Assigned Message", payload.getIssue().toString());
        jmsTemplate.convertAndSend(ASSIGNED_EVENT, payload);
        break;
      case OPENED_EVENT:
        logger.info("Sending Opened Message", payload.getPull_request().toString());
        jmsTemplate.convertAndSend(OPENED_PULL_REQUEST, payload);
        break;
      case CLOSED_EVENT:
        logger.info("Sending Closed Message", payload.getPull_request().toString());
        jmsTemplate.convertAndSend(CLOSED_PULL_REQUEST, payload);
        break;
      default:
        return new ResponseEntity(HttpStatus.NOT_MODIFIED);
    }
    return new ResponseEntity(HttpStatus.OK);
  }

}
