package me.ebenezergraham.honours.platform.controller;

import me.ebenezergraham.honours.platform.model.Issue;
import me.ebenezergraham.honours.platform.model.TwoFactor;
import me.ebenezergraham.honours.platform.model.User;
import me.ebenezergraham.honours.platform.repository.AllocatedIssueRepository;
import me.ebenezergraham.honours.platform.repository.AuthenticationRepository;
import me.ebenezergraham.honours.platform.repository.RewardRepository;
import me.ebenezergraham.honours.platform.repository.UserRepository;
import me.ebenezergraham.honours.platform.security.UserPrincipal;
import me.ebenezergraham.honours.platform.services.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Ebenezer Graham
 * Created on 9/30/19
 */
@RestController
@RequestMapping("/api/v1")
public class UserController {

  private final AuthenticationManager authenticationManager;
  private final UserRepository userRepository;
  private final RewardRepository roleRepository;
  private final PasswordEncoder passwordEncoder;
  private final EmailService emailService;
  private final AuthenticationRepository authenticationRepository;
  private final AllocatedIssueRepository allocatedIssueRepository;

  public UserController(AuthenticationManager authenticationManager,
                        UserRepository userRepository,
                        RewardRepository roleRepository,
                        PasswordEncoder passwordEncoder,
                        EmailService emailService,
                        AuthenticationRepository authenticationRepository,
                        AllocatedIssueRepository allocatedIssueRepository
  ) {
    this.authenticationManager = authenticationManager;
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.passwordEncoder = passwordEncoder;
    this.emailService = emailService;
    this.authenticationRepository = authenticationRepository;
    this.allocatedIssueRepository = allocatedIssueRepository;
  }

  @PostMapping("/code")
  public ResponseEntity<Boolean> verify(@RequestBody TwoFactor request) {
    Optional<TwoFactor> twoFactor = authenticationRepository.findTwoFactorByUsername(request.getUsername());
    if (twoFactor.isPresent()) {
      if (twoFactor.get().getCode().equals(request.getCode())) {
        authenticationRepository.deleteById(twoFactor.get().getId());
        return ResponseEntity.ok(true);
      }
    }
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
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
  public ResponseEntity<Map<String,Object>> fetchPoint(Authentication authentication) {
    Optional<User> user = userRepository.findByEmail(((UserPrincipal)authentication.getPrincipal()).getEmail());

    if (user.isPresent()) {
      Optional<List<Issue>> issues = allocatedIssueRepository.findIssuesByAssigneeName(user.get().getUsername());
      HashMap<String,Object> response = new HashMap<>();
      response.put("points",user.get().getPoints());
      if(issues.isPresent()){
        response.put("issues",issues);
      };
      return ResponseEntity.ok(response);
    }
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

}
