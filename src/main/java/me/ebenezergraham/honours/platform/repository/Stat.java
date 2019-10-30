package me.ebenezergraham.honours.platform.repository;

/**
 @author Ebenezer Graham
 Created on 9/30/19
 */
import javax.persistence.*;

@Entity
public class Stat {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private String id;
    @Column(name = "online_users")
    private String onlineUsers;
    @Column(name = "vulnerabilities_detected")
    private String vulnerabilitiesDetected;

    public Stat() {
    }

    public String getOnlineUsers() {
        return onlineUsers;
    }

    public void setOnlineUsers(String onlineUsers) {
        this.onlineUsers = onlineUsers;
    }

    public String getVulnerabilitiesDetected() {
        return vulnerabilitiesDetected;
    }

    public void setVulnerabilitiesDetected(String vulnerabilitiesDetected) {
        this.vulnerabilitiesDetected = vulnerabilitiesDetected;
    }
}
