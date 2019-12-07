package me.ebenezergraham.honours.platform.model;

import me.ebenezergraham.honours.platform.model.audit.DateAudit;

import javax.persistence.*;
import java.util.List;

/**
 @author Ebenezer Graham
 Created on 9/30/19
 */
@Entity
@Table(name = "Issues")
public class Issue extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "issue_url")
    private String issue;

    private String contributor;

    public Issue() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIssue_url() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getContributor() {
        return contributor;
    }

    public void setContributor(String contributor) {
        this.contributor = contributor;
    }
}