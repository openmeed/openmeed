package me.ebenezergraham.honours.platform.model;

import java.io.Serializable;

public class Payload implements Serializable {

    String action;
    int number;
    PullRequest pull_request;
    Issue issue;
    GithubRepository repository;
    GitHubUser sender;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public PullRequest getPull_request() {
        return pull_request;
    }

    public void setPull_request(PullRequest pull_request) {
        this.pull_request = pull_request;
    }

    public GithubRepository getRepository() {
        return repository;
    }

    public void setRepository(GithubRepository repository) {
        this.repository = repository;
    }

    public GitHubUser getSender() {
        return sender;
    }

    public void setSender(GitHubUser sender) {
        this.sender = sender;
    }

    public Issue getIssue() {
        return issue;
    }

    public void setIssue(Issue issue) {
        this.issue = issue;
    }

    @Override
    public String toString() {
        return "Payload{" +
                "action='" + action + '\'' +
                ", number=" + number +
                ", pull_request=" + pull_request +
                ", repository=" + repository +
                ", sender=" + sender +
                '}';
    }
}
