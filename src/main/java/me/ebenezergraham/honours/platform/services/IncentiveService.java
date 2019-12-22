package me.ebenezergraham.honours.platform.services;

import me.ebenezergraham.honours.platform.model.Reward;
import me.ebenezergraham.honours.platform.repository.AllocatedIssueRepository;
import me.ebenezergraham.honours.platform.repository.RewardRepository;
import me.ebenezergraham.honours.platform.repository.UserRepository;
import org.springframework.stereotype.Service;

/**
 * @author Ebenezer Graham
 * Created on 9/30/19
 */
@Service
public class IncentiveService {

  private final AllocatedIssueRepository allocatedIssueRepository;
  private final RewardRepository rewardRepository;
  private final UserRepository userRepository;
  private final EmailService emailService;


  public IncentiveService(UserRepository userRepository,
                          RewardRepository rewardRepository,
                          AllocatedIssueRepository allocatedIssueRepository,
                          EmailService emailService) {

    this.allocatedIssueRepository = allocatedIssueRepository;
    this.userRepository = userRepository;
    this.rewardRepository = rewardRepository;
    this.emailService = emailService;
  }


  public Reward storeIncentive(Reward reward) {
    try {
      Reward res = rewardRepository.save(reward);
      return res;
    } catch (Exception e) {
      return null;
    }
  }

}
