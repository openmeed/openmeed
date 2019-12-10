package me.ebenezergraham.honours.platform.controller;

import me.ebenezergraham.honours.platform.model.Issue;
import me.ebenezergraham.honours.platform.model.Project;
import me.ebenezergraham.honours.platform.model.Reward;
import me.ebenezergraham.honours.platform.repository.ActivatedRepository;
import me.ebenezergraham.honours.platform.repository.AllocatedIssueRepository;
import me.ebenezergraham.honours.platform.repository.RewardRepository;
import me.ebenezergraham.honours.platform.security.CustomUserDetailsService;
import me.ebenezergraham.honours.platform.security.UserPrincipal;
import me.ebenezergraham.honours.platform.services.IncentiveService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
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
  private final AllocatedIssueRepository allocatedIssueRepository;
  private final ActivatedRepository activatedRepository;
  private final IncentiveService incentiveService;

  public IssuesController(ActivatedRepository activatedRepository,
                          RewardRepository rewardRepository,
                          AllocatedIssueRepository allocatedIssueRepository,
                          IncentiveService incentiveService,
                          CustomUserDetailsService userService) {
    this.incentiveService = incentiveService;
    this.rewardRepository = rewardRepository;
    this.allocatedIssueRepository = allocatedIssueRepository;
    this.activatedRepository = activatedRepository;

  }

  @PostMapping("issue/incentive")
  public ResponseEntity<?> assignIncentive(@Valid @RequestBody Reward reward, Authentication authentication) {
    List<String> authorities =new ArrayList<String>();
    authorities.add(((UserPrincipal) authentication.getPrincipal()).getUsername());
    reward.setAuthorizer(authorities);
    if (incentiveService.storeIncentive(reward) != null) {
      return ResponseEntity.ok().build();
    }
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/issues")
  public ResponseEntity<List<Reward>> findAllIssues(){
      return ResponseEntity.ok(rewardRepository.findAll());
  }
}
