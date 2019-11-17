package me.ebenezergraham.honours.platform.controller;

import me.ebenezergraham.honours.platform.model.Reward;
import me.ebenezergraham.honours.platform.payload.LoginRequest;
import me.ebenezergraham.honours.platform.repository.RewardRepository;
import me.ebenezergraham.honours.platform.security.CustomUserDetailsService;
import me.ebenezergraham.honours.platform.services.EmailService;
import me.ebenezergraham.honours.platform.services.FileService;
import me.ebenezergraham.honours.platform.services.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * @author Ebenezer Graham
 * Created on 9/30/19
 */
@Controller
@RequestMapping("/api/v1")
public class ApplicationController {

    private final EmailService emailService;
    private final CustomUserDetailsService userService;
    private final ReportService reportService;
    private final RewardRepository rewardRepository;

    public ApplicationController(RewardRepository rewardRepository, EmailService emailService, CustomUserDetailsService userService, ReportService reportService) {
        this.emailService = emailService;
        this.userService = userService;
        this.reportService = reportService;
        this.rewardRepository = rewardRepository;

    }

    @PostMapping("/reward")
    public ResponseEntity<?> saveReward(@Valid @RequestBody Reward reward) {
    	Reward res = rewardRepository.save(reward);
    	return ResponseEntity.ok(res);
    }


}
