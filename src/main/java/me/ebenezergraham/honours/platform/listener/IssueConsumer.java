package me.ebenezergraham.honours.platform.listener;

import me.ebenezergraham.honours.platform.model.Payload;
import me.ebenezergraham.honours.platform.repository.AllocatedIssueRepository;
import me.ebenezergraham.honours.platform.repository.RewardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import static me.ebenezergraham.honours.platform.util.Constants.*;

@Component
public class IssueConsumer {
    private final Logger logger = LoggerFactory.getLogger(IssueConsumer.class);
    private final AllocatedIssueRepository allocatedIssueRepository;
    private final RewardRepository rewardRepository;

    public IssueConsumer(final AllocatedIssueRepository allocatedIssueRepository,
                         final RewardRepository rewardRepository) {
        this.allocatedIssueRepository = allocatedIssueRepository;
        this.rewardRepository = rewardRepository;
    }

    @JmsListener(destination = ISSUE_EVENT_TYPE)
    public void assignedIssueListener( Payload message){
        logger.info("Processing {} event from GitHub", message.getAction());
        if(rewardRepository.findRewardByIssueId(message.getIssue().getUrl()).isPresent()){
            allocatedIssueRepository.save(message.getIssue());
        }
    }
}
