package me.ebenezergraham.honours.platform.controller;

import me.ebenezergraham.honours.platform.services.EmailService;
import me.ebenezergraham.honours.platform.services.FileService;
import me.ebenezergraham.honours.platform.services.ReportService;
import me.ebenezergraham.honours.platform.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 @author Ebenezer Graham
 Created on 9/30/19
 */
@Controller
@RequestMapping("/api")
//@PreAuthorize("hasRole('ROLE_USER')")
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
}
