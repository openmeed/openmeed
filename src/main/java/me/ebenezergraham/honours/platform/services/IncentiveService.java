package me.ebenezergraham.honours.platform.services;

import me.ebenezergraham.honours.platform.model.Reward;
import me.ebenezergraham.honours.platform.repository.RewardRepository;
import me.ebenezergraham.honours.platform.security.UserPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Ebenezer Graham
 * Created on 9/30/19
 */
@Service
public class IncentiveService {

  private final RewardRepository rewardRepository;


  public IncentiveService(RewardRepository rewardRepository) {
    this.rewardRepository = rewardRepository;
  }


  public Optional<Reward> storeIncentive(Reward reward, Authentication authentication) {
    List<String> authorities = new ArrayList<>();
    authorities.add(((UserPrincipal) authentication.getPrincipal()).getUsername());
    reward.setAuthorizer(authorities);
    Reward res = rewardRepository.save(reward);
    return Optional.of(res);
  }
  public Optional<Reward> storeIncentive(Reward reward) {
    List<String> authorities = new ArrayList<>();
    reward.setAuthorizer(authorities);
    Reward res = rewardRepository.save(reward);
    return Optional.of(res);
  }
}
