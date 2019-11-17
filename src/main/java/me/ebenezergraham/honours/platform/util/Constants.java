package me.ebenezergraham.honours.platform.util;

public interface Constants {
    String DESTINATION = "github-events";
    String SELECTOR_NAME = "action";

    String PULL_REQUEST = "pull-request";
    String SELECTOR_PULL_REQUEST = SELECTOR_NAME + " = '" + PULL_REQUEST + "'";

}
