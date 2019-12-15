package me.ebenezergraham.honours.platform.controller;

import me.ebenezergraham.honours.platform.model.Project;
import me.ebenezergraham.honours.platform.model.Reward;
import me.ebenezergraham.honours.platform.repository.ActivatedRepository;
import me.ebenezergraham.honours.platform.repository.AllocatedIssueRepository;
import me.ebenezergraham.honours.platform.repository.RewardRepository;
import me.ebenezergraham.honours.platform.security.CustomUserDetailsService;
import me.ebenezergraham.honours.platform.security.UserPrincipal;
import me.ebenezergraham.honours.platform.services.IncentiveService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
/**
 * @author Ebenezer Graham
 * Created on 9/30/19
 */
@Controller
public class ApplicationController {

  public ApplicationController() {
  }

  @GetMapping("/test")
  public ResponseEntity<String> test() {
    return ResponseEntity.ok("Test");
  }

  @GetMapping("/")
  public ModelAndView index() {
    return new ModelAndView("index.html");
  }

}
