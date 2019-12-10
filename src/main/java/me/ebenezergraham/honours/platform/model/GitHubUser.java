package me.ebenezergraham.honours.platform.model;

import java.io.Serializable;

public class GitHubUser implements Serializable {
    int id;
    String login;
    String type;
    String site_admin;
    String url;

    public GitHubUser() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id){
        this.id = id;
    };

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSite_admin() {
        return site_admin;
    }

    public void setSite_admin(String site_admin) {
        this.site_admin = site_admin;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
