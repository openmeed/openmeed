package me.ebenezergraham.honours.platform.controller;

import me.ebenezergraham.honours.platform.model.Payload;
import me.ebenezergraham.honours.platform.services.RewardEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
  public ResponseEntity<String> publish(@RequestBody final Payload payload, @RequestHeader(X_GitHub_Event) String eventType, final @RequestHeader Map<String,String> header) {
    logger.info(eventType);
    if (payload.getPull_request() != null) {
      logger.info("Sending Pull Request Payload", payload.getPull_request().toString());
      jmsTemplate.convertAndSend(PULL_REQUEST_EVENT.concat("_").concat(payload.getAction()), payload);
    } else if (payload.getIssue() != null) {
      logger.info("Sending Issue Payload", payload.getIssue().toString());
      jmsTemplate.convertAndSend(ISSUES_EVENT.concat("_").concat(payload.getAction()), payload);

    }else if (payload.getRepository() != null && payload.getIssue()==null && payload.getPull_request()==null) {
      logger.info("Project event ", payload.getRepository().getName());
      jmsTemplate.convertAndSend(PROJECT_EVENT.concat("_").concat(payload.getAction()), payload);
    }
    return new ResponseEntity(HttpStatus.OK);
}

}
