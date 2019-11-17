package me.ebenezergraham.honours.platform.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import me.ebenezergraham.honours.platform.model.audit.DateAudit;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

/**
 @author Ebenezer Graham
 Created on 9/30/19
 */
@Entity
@Table(name = "rewards")
public class Reward extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String issueId;

    @Column(nullable = false)
    private String repositoryId;

    @Column(nullable = false)
    private String rewardType;

    private String rewardValue;

    @Column(nullable = false)
    private Boolean assignedUser = false;

    public Reward() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIssueId() {
        return issueId;
    }

    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }

    public String getRewardType() {
        return rewardType;
    }

    public void setRewardType(String rewardType) {
        this.rewardType = rewardType;
    }

    public String getRewardValue() {
        return rewardValue;
    }

    public void setRewardValue(String rewardValue) {
        this.rewardValue = rewardValue;
    }

    public Boolean getAssignedUser() {
        return assignedUser;
    }

    public void setAssignedUser(Boolean assignedUser) {
        this.assignedUser = assignedUser;
    }
}


