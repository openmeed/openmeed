package me.ebenezergraham.ssd.snippetanalyser.configuration;

import me.ebenezergraham.ssd.snippetanalyser.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

/**
 @author Ebenezer Graham
 Created on 7/18/19
 */

@Configuration
@EnableWebSecurity
public class HttpConfiguration extends WebSecurityConfigurerAdapter {
	@Autowired
	private WebApplicationContext applicationContext;
	private CustomUserDetailsService userDetailsService;
	@Autowired
	private AuthenticationSuccessHandlerImpl successHandler;
	@Autowired
	private DataSource dataSource;
	
	@PostConstruct
	public void completeSetup() {
		userDetailsService = applicationContext.getBean(CustomUserDetailsService.class);
	}
	
	@Override
	protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService)
				.passwordEncoder(encoder())
				.and()
				.authenticationProvider(authenticationProvider())
				.jdbcAuthentication()
				.dataSource(dataSource);
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**");
	}
	
	@Override
	protected void configure(final HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/login")
				.permitAll()
				.and()
					.formLogin()
					.loginPage("/login.html")
					.defaultSuccessUrl("/homepage.html", true)
					.permitAll()
					.successHandler(successHandler)
				.and()
					.csrf()
					.disable();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		final DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(encoder());
		return authProvider;
	}
	
	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder(11);
	}
	
	@Bean
	public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
		return new SecurityEvaluationContextExtension();
	}
}