package me.ebenezergraham.honours.platform.model;

import java.io.Serializable;
import java.util.Date;

public class Review implements Serializable {
    int id;
    String node_id;
    GitHubUser user;
    String body;
    String state;
    Date submitted_at;
    String pull_request_url;
    String author_association;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNode_id() {
        return node_id;
    }

    public void setNode_id(String node_id) {
        this.node_id = node_id;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getSubmitted_at() {
        return submitted_at;
    }

    public void setSubmitted_at(Date submitted_at) {
        this.submitted_at = submitted_at;
    }

    public String getPull_request_url() {
        return pull_request_url;
    }

    public void setPull_request_url(String pull_request_url) {
        this.pull_request_url = pull_request_url;
    }

    public String getAuthor_association() {
        return author_association;
    }

    public void setAuthor_association(String author_association) {
        this.author_association = author_association;
    }
}
