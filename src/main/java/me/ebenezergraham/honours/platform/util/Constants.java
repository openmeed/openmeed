package me.ebenezergraham.honours.platform.util;

public interface Constants {
  //Request Headers
  String X_GitHub_Event = "X-GitHub-Event";
  //Event actions in a typical payload
  String CLOSED_ACTION = "closed";
  String OPENED_ACTION = "opened";
  String ASSIGNED_ACTION = "assigned";
  String CLAIMED_ACTION = "claimed";
  String DELETED_ACTION = "deleted";
  String EDITED_ACTION = "edited";
  String SUBMITTED_ACTION = "submitted";
  String REVIEW_REQUESTED_ACTION = "review_requested";
  String APPROVED_ACTION = "approved";
  // System Events
  String EMAIL_EVENT = "email";
  // GitHub Events
  String ISSUES_EVENT = "issues";
  String ISSUE_COMMENT_EVENT = "issue_comment";
  String PULL_REQUEST_EVENT = "pull_request";
  String PULL_REQUEST_REVIEW_EVENT = "pull_request_review";
  String PROJECT_EVENT = "project";
  // JMS event selectors
  String PULL_REQUEST_EVENT_CLOSED_ACTION_SELECTOR = PULL_REQUEST_EVENT + "_" + CLOSED_ACTION;
  String PULL_REQUEST_EVENT_EDITED_ACTION_SELECTOR = PULL_REQUEST_EVENT + "_" + EDITED_ACTION;
  String PULL_REQUEST_REVIEW_EVENT_APPROVED_SELECTOR = PULL_REQUEST_EVENT + "_" + APPROVED_ACTION;
  String ISSUE_EVENT_ASSIGNED_ACTION_SELECTOR = ISSUES_EVENT + "_" + ASSIGNED_ACTION;
  String PROJECT_EVENT_DELETED_ACTION_SELECTOR = PROJECT_EVENT + "_" + DELETED_ACTION;
  String EMAIL_EVENT_CLAIMED_ACTION_SELECTOR = EMAIL_EVENT + "_" + CLAIMED_ACTION;
}
