package me.ebenezergraham.honours.platform.services;

import me.ebenezergraham.honours.platform.model.TwoFactor;
import me.ebenezergraham.honours.platform.model.User;
import me.ebenezergraham.honours.platform.payload.LoginRequest;
import me.ebenezergraham.honours.platform.repository.AuthenticationRepository;
import me.ebenezergraham.honours.platform.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.jetbrains.annotations.NotNull;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public class RecentLogin extends Thread {

    HttpServletRequest request;
    LoginRequest loginRequest;
    UserRepository userRepository;
    EmailService emailService;
    String address;
    AuthenticationRepository authenticationRepository;

    public RecentLogin(@NotNull String name,
                       final HttpServletRequest request,
                       LoginRequest loginRequest,
                       UserRepository userRepository,
                       EmailService emailService,
                       String address,
                       AuthenticationRepository authenticationRepository) {
        super(name);
        this.request = request;
        this.loginRequest = loginRequest;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.address = address;
        this.authenticationRepository = authenticationRepository;
    }

    public void run() {
        System.out.println(this.getName() + ": New Thread is running...");
        System.out.println(request.toString());
        try {
            User user = null;
            if (!loginRequest.getUsernameOrEmail().contains("@")) {
                user = userRepository.findByUsername(loginRequest.getUsernameOrEmail()).get();
            } else {
                user = userRepository.findByEmail(loginRequest.getUsernameOrEmail()).get();
            }
            if (loginRequest.isTwoFA()) {
                Optional<TwoFactor> res = authenticationRepository.findTwoFactorByUsername(loginRequest.getUsernameOrEmail());
                if (res.isPresent()) {
                    authenticationRepository.deleteById(res.get().getId());
                }
                String token = RandomStringUtils.randomNumeric(6);
                TwoFactor result = authenticationRepository.save(new TwoFactor(loginRequest.getUsernameOrEmail(), token));
                emailService.sendSimpleMessage(user.getEmail(), "Two-Factor Authentication", "Token: " + result.getCode());
            }
            if (user != null) {
                emailService.sendSimpleMessage(user.getEmail(), "Recent Login",
                        "Dear " + user.getName()
                                + ",\n\nYou recently logged In from "
                                + address + "\nIf this wasn't, please escalate this issue to us at report.sanalyzer@gmail.com \n\nAt your service, \nEbenezer Graham ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
