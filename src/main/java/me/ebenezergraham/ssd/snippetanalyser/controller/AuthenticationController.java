package me.ebenezergraham.ssd.snippetanalyser.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 @author Ebenezer Graham
 Created on 7/18/19
 */

@Controller
public class AuthenticationController {
	
	@GetMapping("/login")
	public String login(@RequestParam(name = "name", required = false, defaultValue = "World") String name, Model model) {
		model.addAttribute("name", name);
		return "login";
	}
	
	@GetMapping("/register")
	public String register() {
		return "register";
	}
	
}
