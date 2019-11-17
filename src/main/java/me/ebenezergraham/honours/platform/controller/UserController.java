package me.ebenezergraham.honours.platform.controller;

import me.ebenezergraham.honours.platform.model.Role;
import me.ebenezergraham.honours.platform.model.RoleName;
import me.ebenezergraham.honours.platform.model.TwoFactor;
import me.ebenezergraham.honours.platform.model.User;
import me.ebenezergraham.honours.platform.payload.ApiResponse;
import me.ebenezergraham.honours.platform.payload.LoginRequest;
import me.ebenezergraham.honours.platform.payload.SignUpRequest;
import me.ebenezergraham.honours.platform.repository.AuthenticationRepository;
import me.ebenezergraham.honours.platform.repository.RewardRepository;
import me.ebenezergraham.honours.platform.repository.UserRepository;
import me.ebenezergraham.honours.platform.services.EmailService;
import me.ebenezergraham.honours.platform.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

/**
 @author Ebenezer Graham
 Created on 9/30/19
 */
@RestController
@RequestMapping("/api/v1/auth")
public class UserController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RewardRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final AuthenticationRepository authenticationRepository;

    public UserController(AuthenticationManager authenticationManager,
                          UserRepository userRepository,
                          RewardRepository roleRepository,
                          PasswordEncoder passwordEncoder,
                          EmailService emailService,
                          AuthenticationRepository authenticationRepository
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.authenticationRepository = authenticationRepository;
    }

    @PostMapping("/code")
    public ResponseEntity<Boolean> verify(@RequestBody TwoFactor request) {
        Optional<TwoFactor> twoFactor = authenticationRepository.findTwoFactorByUsername(request.getUsername());
        if (twoFactor.isPresent()) {
            if (twoFactor.get().getCode().equals(request.getCode())) {
                authenticationRepository.deleteById(twoFactor.get().getId());
                return ResponseEntity.ok(true);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

}
