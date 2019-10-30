package me.ebenezergraham.honours.platform.model;
/**
 @author Ebenezer Graham
 Created on 9/30/19
 */
import me.ebenezergraham.honours.platform.model.audit.DateAudit;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class TwoFactor extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String username;
    private String code;

    public TwoFactor() {
    }

    public TwoFactor(String username, String code) {
        this.username = username;
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
