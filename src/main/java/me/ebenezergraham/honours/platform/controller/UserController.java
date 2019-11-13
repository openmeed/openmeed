package me.ebenezergraham.honours.platform.controller;

import me.ebenezergraham.honours.platform.exception.ResourceNotFoundException;
import me.ebenezergraham.honours.platform.model.Role;
import me.ebenezergraham.honours.platform.model.RoleName;
import me.ebenezergraham.honours.platform.model.TwoFactor;
import me.ebenezergraham.honours.platform.model.User;
import me.ebenezergraham.honours.platform.payload.ApiResponse;
import me.ebenezergraham.honours.platform.payload.LoginRequest;
import me.ebenezergraham.honours.platform.payload.SignUpRequest;
import me.ebenezergraham.honours.platform.repository.AuthenticationRepository;
import me.ebenezergraham.honours.platform.repository.RoleRepository;
import me.ebenezergraham.honours.platform.repository.UserRepository;
import me.ebenezergraham.honours.platform.security.CurrentUser;
import me.ebenezergraham.honours.platform.security.UserPrincipal;
import me.ebenezergraham.honours.platform.services.EmailService;
import me.ebenezergraham.honours.platform.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import java.util.Collections;
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
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final AuthenticationRepository authenticationRepository;

    public UserController(AuthenticationManager authenticationManager,
                          UserRepository userRepository,
                          RoleRepository roleRepository,
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

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public User getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        return userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient,
                                              @AuthenticationPrincipal OAuth2User oauth2User,@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request) {

        /*if (oauth2User.getAttributes().get("id") != null) {
            RecentLogin recentLogin = new RecentLogin("Recent Login", request, loginRequest, userRepository, emailService, request.getRemoteAddr(), authenticationRepository);
            recentLogin.start();
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);*/


        return ResponseEntity.ok(authorizedClient);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity(new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity(new ApiResponse(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }

        User user = new User(signUpRequest.getName(), signUpRequest.getUsername(),
                signUpRequest.getEmail(), signUpRequest.getPassword());

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new AppException("User Role not set."));

        user.setRoles(Collections.singleton(userRole));

        User result = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(result.getUsername()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered"));
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
