package me.ebenezergraham.ssd.snippetanalyser.services;

import me.ebenezergraham.ssd.snippetanalyser.models.UserPrincipal;
import me.ebenezergraham.ssd.snippetanalyser.repository.User;
import me.ebenezergraham.ssd.snippetanalyser.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;

/**
 @author Ebenezer Graham
 Created on 7/18/19
 */

@Service
public class CustomUserDetailsService implements UserDetailsService {
	
	@Autowired
	private WebApplicationContext applicationContext;
	private UserRepository userRepository;
	
	public CustomUserDetailsService() {
		super();
	}
	
	@PostConstruct
	public void completeSetup() {
		userRepository = applicationContext.getBean(UserRepository.class);
	}
	
	@Override
	public UserDetails loadUserByUsername(final String username) {
		final User user = userRepository.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException(username);
		}
		return new UserPrincipal(user);
	}
}
