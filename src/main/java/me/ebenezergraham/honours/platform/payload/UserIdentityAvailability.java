package me.ebenezergraham.honours.platform.payload;

/**
 @author Ebenezer Graham
 Created on 9/30/19
 */
public class UserIdentityAvailability {
    private Boolean available;

    public UserIdentityAvailability(Boolean available) {
        this.available = available;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }
}
