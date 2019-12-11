package me.ebenezergraham.honours.platform.controller;

import me.ebenezergraham.honours.platform.model.Project;
import me.ebenezergraham.honours.platform.repository.ActivatedRepository;
import me.ebenezergraham.honours.platform.repository.AllocatedIssueRepository;
import me.ebenezergraham.honours.platform.repository.RewardRepository;
import me.ebenezergraham.honours.platform.security.CustomUserDetailsService;
import me.ebenezergraham.honours.platform.services.IncentiveService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * @author Ebenezer Graham
 * Created on 9/30/19
 */
@Controller
@RequestMapping("/api/v1")
public class ProjectController {

  private final RewardRepository rewardRepository;
  private final AllocatedIssueRepository allocatedIssueRepository;
  private final ActivatedRepository activatedRepository;
  private final IncentiveService incentiveService;

  public ProjectController(ActivatedRepository activatedRepository,
                           RewardRepository rewardRepository,
                           AllocatedIssueRepository allocatedIssueRepository,
                           IncentiveService incentiveService,
                           CustomUserDetailsService userService) {
    this.incentiveService = incentiveService;
    this.rewardRepository = rewardRepository;
    this.allocatedIssueRepository = allocatedIssueRepository;
    this.activatedRepository = activatedRepository;

  }

  @PostMapping("/projects")
  public ResponseEntity<?> assignIssue(@Valid @RequestBody String[] projects) {
    List<String> project = Arrays.asList(projects);
    project.forEach(repository -> {
      activatedRepository.save(new Project(repository));
    });
    return ResponseEntity.ok().build();
  }

  @GetMapping("/projects")
  public ResponseEntity<?> findAllActivatedProjects() {
    return ResponseEntity.ok(activatedRepository.findAll());
  }
}
