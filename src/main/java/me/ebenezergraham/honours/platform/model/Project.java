package me.ebenezergraham.honours.platform.model;

import me.ebenezergraham.honours.platform.model.audit.DateAudit;

import javax.persistence.*;

/**
 @author Ebenezer Graham
 Created on 9/30/19
 */
@Entity
@Table(name = "projects")
public class Project extends DateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "full_name")
    private String fullName;

    public Project() {

    }
    public Project(String fullName) {
        this.fullName = fullName;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}