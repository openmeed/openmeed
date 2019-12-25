package me.ebenezergraham.honours.platform.util;

public interface Constants {
  String CLOSED_ACTION = "closed";
  String OPENED_ACTION = "opened";
  String ASSIGNED_ACTION = "assigned";
  String CLAIMED_ACTION = "claimed";
  String DELETED_ACTION = "deleted";

  String EMAIL_EVENT = "email";
  String ISSUE_EVENT = "issue";
  String PULL_REQUEST_EVENT = "pull-request";
  String PROJECT_EVENT = "project";

  String PULL_REQUEST_EVENT_CLOSED_ACTION_SELECTOR = PULL_REQUEST_EVENT+"_"+CLOSED_ACTION;
  String ISSUE_EVENT_ASSIGNED_ACTION_SELECTOR = ISSUE_EVENT+"_"+ASSIGNED_ACTION;
  String PROJECT_EVENT_DELETED_ACTION_SELECTOR = PROJECT_EVENT+"_"+ DELETED_ACTION;
  String EMAIL_EVENT_CLAIMED_ACTION_SELECTOR = EMAIL_EVENT+"_"+CLAIMED_ACTION;

}
