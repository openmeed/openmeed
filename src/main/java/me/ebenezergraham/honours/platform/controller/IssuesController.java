package me.ebenezergraham.honours.platform.controller;

import me.ebenezergraham.honours.platform.model.Reward;
import me.ebenezergraham.honours.platform.repository.RewardRepository;
import me.ebenezergraham.honours.platform.services.IncentiveService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * @author Ebenezer Graham
 * Created on 9/30/19
 */
@Controller
@RequestMapping("/api/v1")
public class IssuesController {

  private final RewardRepository rewardRepository;
  private final IncentiveService incentiveService;

  public IssuesController(RewardRepository rewardRepository,
                          IncentiveService incentiveService) {
    this.incentiveService = incentiveService;
    this.rewardRepository = rewardRepository;
  }

  @PostMapping("issue/incentive")
  public ResponseEntity<Reward> assignIncentive(@Valid @RequestBody Reward reward, Authentication authentication) {
    Optional<Reward> result = incentiveService.storeIncentive(reward, authentication);
    if (result.isPresent()) {
      return ResponseEntity.ok(result.get());
    }
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/issue/incentive")
  public ResponseEntity<List<Reward>> findAllIssues() {
    return ResponseEntity.ok(rewardRepository.findAll());
  }
}