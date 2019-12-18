package me.ebenezergraham.honours.platform.listener;

import me.ebenezergraham.honours.platform.model.Payload;
import me.ebenezergraham.honours.platform.repository.ActivatedRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import static me.ebenezergraham.honours.platform.util.Constants.PROJECT_EVENT_TYPE;

@Component
public class ProjectConsumer {
  private final Logger logger = LoggerFactory.getLogger(ProjectConsumer.class);
  private final ActivatedRepository activatedRepository;

  public ProjectConsumer(final ActivatedRepository activatedRepository) {
    this.activatedRepository = activatedRepository;
  }

  @JmsListener(destination = PROJECT_EVENT_TYPE)
  private void notifyContributor(Payload payload) {
    logger.info("Deleting Project Repository");
    if (payload.getAction().equals("deleted")) {
      activatedRepository.findProjectByFullName(payload.getRepository().getFull_name());
    }
    activatedRepository.findProjectByFullName("Pactmart/test").ifPresent( project -> {
      activatedRepository.delete(project);
    });
  }
}
