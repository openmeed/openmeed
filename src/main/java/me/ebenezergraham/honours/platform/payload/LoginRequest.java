package me.ebenezergraham.honours.platform.payload;

import javax.validation.constraints.NotBlank;

/**
 @author Ebenezer Graham
 Created on 9/30/19
 */
public class LoginRequest {
    @NotBlank
    private String usernameOrEmail;

    @NotBlank
    private String password;

    private boolean twoFA;

    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }

    public void setUsernameOrEmail(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isTwoFA() {
        return twoFA;
    }

    public void setTwoFA(boolean twoFA) {
        this.twoFA = twoFA;
    }
}
