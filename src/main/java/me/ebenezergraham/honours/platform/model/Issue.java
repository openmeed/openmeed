package me.ebenezergraham.honours.platform.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
@Entity
@Table(name = "Issues", uniqueConstraints = {
    @UniqueConstraint(columnNames = {
        "issue_url"
    })
})
public class Issue implements Serializable {

    @Id
    @Column(name="issue_url")
    String url;
    int id;
    String html_url;
    String state;
    int number;
    String title;
    @Transient
    GitHubUser user;
    Date created_at;
    Date updated_at;
    Date closed_at;
    boolean locked;
    @Transient
    GitHubUser assignee;
    @Transient
    GitHubUser [] assignees;
    @Transient
    Labels[] labels;
    String author_association;
    String assigneeName;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHtml_url() {
        return html_url;
    }

    public void setHtml_url(String html_url) {
        this.html_url = html_url;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public GitHubUser getUser() {
        return user;
    }

    public void setUser(GitHubUser user) {
        this.user = user;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public Date getClosed_at() {
        return closed_at;
    }

    public void setClosed_at(Date closed_at) {
        this.closed_at = closed_at;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public GitHubUser getAssignee() {
        return assignee;
    }

    @Column(name="assignee")
    public String getAssigneeName() {
        return assigneeName;
    }

    public void setAssigneeName(String assigneeName) {
        this.assigneeName = assignee.getLogin();
    }

    public void setAssignee(GitHubUser assignee) {
        this.assignee = assignee;
    }

    public GitHubUser[] getAssignees() {
        return assignees;
    }

    public void setAssignees(GitHubUser[] assignees) {
        this.assignees = assignees;
    }

    public Labels[] getLabels() {
        return labels;
    }

    public void setLabels(Labels[] labels) {
        this.labels = labels;
    }

    public String getAuthor_association() {
        return author_association;
    }

    public void setAuthor_association(String author_association) {
        this.author_association = author_association;
    }
}
