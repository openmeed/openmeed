package me.ebenezergraham.ssd.snippetanalyser.configuration;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import me.ebenezergraham.ssd.snippetanalyser.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

/**
 @author Ebenezer Graham
 Created on 7/18/19
 */

@Component
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest arg0, HttpServletResponse arg1, Authentication arg2) throws IOException, ServletException {
		userRepository.updateLastLogin(new Date());
	}
}
