package me.ebenezergraham.honours.platform.controller;

import me.ebenezergraham.honours.platform.model.User;
import me.ebenezergraham.honours.platform.services.EmailService;
import me.ebenezergraham.honours.platform.services.FileService;
import me.ebenezergraham.honours.platform.services.ReportService;
import me.ebenezergraham.honours.platform.services.CustomUserDetailsService;
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

import java.util.List;

/**
 @author Ebenezer Graham
 Created on 9/30/19
 */
@Controller
@RequestMapping("/api/v1")
public class ApplicationController {
	
	private final FileService fileAnalyzer;
	private final EmailService emailService;
	private final CustomUserDetailsService userService;
	private final ReportService reportService;
	@Autowired
	private ApplicationContext applicationContext;
	
	public ApplicationController(FileService fileAnalyzer, EmailService emailService, CustomUserDetailsService userService, ReportService reportService) {
		this.fileAnalyzer = fileAnalyzer;
		this.emailService = emailService;
		this.userService = userService;
		this.reportService = reportService;
	}
	
	@PostMapping("/analyze")
	public ResponseEntity<String> analyze(@RequestParam("file") MultipartFile file ,@RequestParam("email") String email) {
		List<String> response = fileAnalyzer.analyze(file);
		emailService.sendMessageWithAttachment(email,
				"Vulnerability Report",
				"Find your Report attached.\nPassword: "+response.get(0), response.get(1));
		/*Resource report = this.applicationContext.getResource(response.get(1));
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + report.getFilename() + "\"")
				.body(report);*/
		return ResponseEntity.ok().body("Sent");
	}


	@RequestMapping("/securedPage")
	public String securedPage(Model model,
							  @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient,
							  @AuthenticationPrincipal OAuth2User oauth2User) {
		model.addAttribute("userName", authorizedClient.getAccessToken().getTokenValue());
		model.addAttribute("clientName", authorizedClient.getClientRegistration().getClientName());
		model.addAttribute("userAttributes", oauth2User.getAttributes());
		return "securedPage";
	}

}
