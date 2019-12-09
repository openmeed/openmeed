package me.ebenezergraham.honours.platform.model;

import me.ebenezergraham.honours.platform.model.audit.DateAudit;

import javax.persistence.*;

/**
 * @author Ebenezer Graham
 * Created on 9/30/19
 */
@Entity
@Table(name = "rewards", uniqueConstraints = {
    @UniqueConstraint(columnNames = {
        "issueId"
    })
})
public class Reward extends DateAudit {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String issueId;

  @Column(nullable = false)
  private String type;

  private String value;

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

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

}


