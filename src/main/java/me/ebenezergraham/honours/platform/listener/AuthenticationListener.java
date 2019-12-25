package me.ebenezergraham.honours.platform.listener;

import me.ebenezergraham.honours.platform.model.User;
import me.ebenezergraham.honours.platform.repository.UserRepository;
import me.ebenezergraham.honours.platform.services.EmailService;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author Ebenezer Graham
 * Created on 9/30/19
 */
@Component
public class AuthenticationListener {

  private UserRepository userRepository;
  private EmailService emailService;

  public AuthenticationListener(EmailService emailService, UserRepository userRepository) {
    this.emailService = emailService;
    this.userRepository = userRepository;
  }

  @EventListener
  public void authenticationFailed(final AuthenticationFailureBadCredentialsEvent event) {
    new Thread(() -> {
      String username = (String) event.getAuthentication().getPrincipal();
      Optional<User> user = userRepository.findByUsername(username);
      user.ifPresent((user1) -> {
        emailService.sendSimpleMessage(user1.getEmail(), "Failed Attempted",
            "Dear " + user1.getName() + ",\n\nSomeone made an attempt to access your account. If it wasn't you consider strengthening your credentials");
      });
    }).start();
  }
}

