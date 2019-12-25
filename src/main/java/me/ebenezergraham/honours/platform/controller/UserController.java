package me.ebenezergraham.honours.platform.controller;

import me.ebenezergraham.honours.platform.model.Issue;
import me.ebenezergraham.honours.platform.model.Reward;
import me.ebenezergraham.honours.platform.model.User;
import me.ebenezergraham.honours.platform.repository.AllocatedIssueRepository;
import me.ebenezergraham.honours.platform.repository.RewardRepository;
import me.ebenezergraham.honours.platform.repository.UserRepository;
import me.ebenezergraham.honours.platform.security.UserPrincipal;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @author Ebenezer Graham
 * Created on 9/30/19
 */
@RestController
@RequestMapping("/api/v1")
public class UserController {

  private final UserRepository userRepository;
  private final AllocatedIssueRepository allocatedIssueRepository;
  private final RewardRepository rewardRepository;

  public UserController(UserRepository userRepository,
                        AllocatedIssueRepository allocatedIssueRepository,
                        RewardRepository rewardRepository
  ) {
    this.userRepository = userRepository;
    this.allocatedIssueRepository = allocatedIssueRepository;
    this.rewardRepository = rewardRepository;
  }

  @GetMapping("/users")
  public ResponseEntity<List<User>> findAllUser() {
    List<User> result = userRepository.findAll();
    if (!result.isEmpty()) {
      return ResponseEntity.ok(result);
    }
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @GetMapping("/user/rewards")
  public ResponseEntity<Map<String, Object>> fetchPoint(Authentication authentication) {
    Optional<User> user = userRepository.findByEmail(((UserPrincipal) authentication.getPrincipal()).getEmail());
    final List<Reward> potentialRewards = new ArrayList<>();
    if (user.isPresent()) {
      Optional<List<Issue>> issues = allocatedIssueRepository.findIssuesByAssigneeName(user.get().getUsername());
      issues.ifPresent(result -> {
        result.forEach((issue -> {
          rewardRepository.findRewardByIssueId(issue.getHtmlUrl()).ifPresent(reward -> {
            potentialRewards.add(reward);
          });
        }));
      });
      HashMap<String, Object> response = new HashMap<>();
      response.put("points", user.get().getPoints());
      if (issues.isPresent()) {
        response.put("potentialRewards", potentialRewards);
      }
      return ResponseEntity.ok(response);
    }
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

}
