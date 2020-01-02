package me.ebenezergraham.honours.platform.model;

import java.io.Serializable;
import java.util.Date;

public class PullRequest implements Serializable {

    String url;
    int id;
    String html_url;
    String state;
    int number;
    String title;
    // PR creator
    GitHubUser user;
    String body;
    Date created_at;
    Date updated_at;
    Date closed_at;
    String issue_url;
    Date merged_at;
    String merge_commit_sha;
    GitHubUser assignee;
    GitHubUser[] assignees;
    String[] requested_reviewers;
    String[] requested_teams;
    String [] labels;
    boolean merged;
    GithubRepository repo;
    // PR action performed this user
    GitHubUser sender;

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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
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

    public Date getMerged_at() {
        return merged_at;
    }

    public void setMerged_at(Date merged_at) {
        this.merged_at = merged_at;
    }

    public String getMerge_commit_sha() {
        return merge_commit_sha;
    }

    public void setMerge_commit_sha(String merge_commit_sha) {
        this.merge_commit_sha = merge_commit_sha;
    }

    public GitHubUser getAssignee() {
        return assignee;
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

    public String[] getLabels() {
        return labels;
    }

    public void setLabels(String[] labels) {
        this.labels = labels;
    }

    public GithubRepository getRepo() {
        return repo;
    }

    public void setRepo(GithubRepository repo) {
        this.repo = repo;
    }

    public GitHubUser getSender() {
        return sender;
    }

    public void setSender(GitHubUser sender) {
        this.sender = sender;
    }

    public String getIssue_url() {
        return issue_url;
    }

    public void setIssue_url(String issue_url) {
        this.issue_url = issue_url;
    }

    public boolean isMerged() {
        return merged;
    }

    public void setMerged(boolean merged) {
        this.merged = merged;
    }

    public String[] getRequested_reviewers() {
        return requested_reviewers;
    }

    public void setRequested_reviewers(String[] requested_reviewers) {
        this.requested_reviewers = requested_reviewers;
    }

    public String[] getRequested_teams() {
        return requested_teams;
    }

    public void setRequested_teams(String[] requested_teams) {
        this.requested_teams = requested_teams;
    }
}
