/*
package me.ebenezergraham.honours.platform;

import me.ebenezergraham.honours.platform.model.*;
import me.ebenezergraham.honours.platform.repository.AllocatedIssueRepository;
import me.ebenezergraham.honours.platform.repository.RewardRepository;
import me.ebenezergraham.honours.platform.repository.UserRepository;
import me.ebenezergraham.honours.platform.services.IncentiveService;
import me.ebenezergraham.honours.platform.services.RewardEngine;
import net.minidev.json.JSONObject;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PlatformApplicationTests {
	
	@Autowired
	RewardRepository rewardRepository;
	@Autowired
	AllocatedIssueRepository allocatedIssueRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	IncentiveService incentiveService;

	@Autowired
	RewardEngine rewardEngine;

	@BeforeClass
	public static void setUp() {

	}

	private String testIssue = "https://github.com/openmeed/rewarder/issues/20";
	private String githubEventEndpoint = "http://localhost:8080/api/v1/github/events";
	private String testUser = "hermes";
	private String sampleEvent = "{ \"action\": \"closed\", \"number\": 2, \"pull_request\": { \"url\": \"https://api.github.com/repos/Codertocat/Hello-World/pulls/2\", \"id\": 279147437, \"issue_url\": \"https://github.com/anoited007/personalized-tab/issues/20\", \"title\": \"Update the README with new information.\", \"user\": { \"login\": \"ebenezergraham\", \"id\": 21031067, \"node_id\": \"MDQ6VXNlcjIxMDMxMDY3\", \"avatar_url\": \"https://avatars1.githubusercontent.com/u/21031067?v=4\", \"gravatar_id\": \"\", \"url\": \"https://api.github.com/users/Codertocat\", \"html_url\": \"https://github.com/Codertocat\", \"followers_url\": \"https://api.github.com/users/Codertocat/followers\", \"following_url\": \"https://api.github.com/users/Codertocat/following{/other_user}\", \"gists_url\": \"https://api.github.com/users/Codertocat/gists{/gist_id}\", \"starred_url\": \"https://api.github.com/users/Codertocat/starred{/owner}{/repo}\", \"subscriptions_url\": \"https://api.github.com/users/Codertocat/subscriptions\", \"organizations_url\": \"https://api.github.com/users/Codertocat/orgs\", \"repos_url\": \"https://api.github.com/users/Codertocat/repos\", \"events_url\": \"https://api.github.com/users/Codertocat/events{/privacy}\", \"received_events_url\": \"https://api.github.com/users/Codertocat/received_events\", \"type\": \"User\", \"site_admin\": false }, \"body\": \"This is a pretty simple change that we need to pull into master.\", \"created_at\": \"2019-05-15T15:20:33Z\", \"updated_at\": \"2019-05-15T15:20:33Z\", \"closed_at\": null, \"merged_at\": null, \"merge_commit_sha\": null, \"assignee\": null, \"assignees\": [ ], \"requested_reviewers\": [ ], \"requested_teams\": [ ], \"labels\": [ ], \"milestone\": null, \"commits_url\": \"https://api.github.com/repos/Codertocat/Hello-World/pulls/2/commits\", \"review_comments_url\": \"https://api.github.com/repos/Codertocat/Hello-World/pulls/2/comments\", \"review_comment_url\": \"https://api.github.com/repos/Codertocat/Hello-World/pulls/comments{/number}\", \"comments_url\": \"https://api.github.com/repos/Codertocat/Hello-World/issues/2/comments\", \"statuses_url\": \"https://api.github.com/repos/Codertocat/Hello-World/statuses/ec26c3e57ca3a959ca5aad62de7213c562f8c821\", \"head\": { \"label\": \"Codertocat:changes\", \"ref\": \"changes\", \"sha\": \"ec26c3e57ca3a959ca5aad62de7213c562f8c821\", \"user\": { \"login\": \"Codertocat\", \"id\": 21031067, \"node_id\": \"MDQ6VXNlcjIxMDMxMDY3\", \"avatar_url\": \"https://avatars1.githubusercontent.com/u/21031067?v=4\", \"gravatar_id\": \"\", \"url\": \"https://api.github.com/users/Codertocat\", \"html_url\": \"https://github.com/Codertocat\", \"followers_url\": \"https://api.github.com/users/Codertocat/followers\", \"following_url\": \"https://api.github.com/users/Codertocat/following{/other_user}\", \"gists_url\": \"https://api.github.com/users/Codertocat/gists{/gist_id}\", \"starred_url\": \"https://api.github.com/users/Codertocat/starred{/owner}{/repo}\", \"subscriptions_url\": \"https://api.github.com/users/Codertocat/subscriptions\", \"organizations_url\": \"https://api.github.com/users/Codertocat/orgs\", \"repos_url\": \"https://api.github.com/users/Codertocat/repos\", \"events_url\": \"https://api.github.com/users/Codertocat/events{/privacy}\", \"received_events_url\": \"https://api.github.com/users/Codertocat/received_events\", \"type\": \"User\", \"site_admin\": false }, \"repo\": { \"id\": 186853002, \"node_id\": \"MDEwOlJlcG9zaXRvcnkxODY4NTMwMDI=\", \"name\": \"Hello-World\", \"full_name\": \"Codertocat/Hello-World\", \"private\": false, \"owner\": { \"login\": \"Codertocat\", \"id\": 21031067, \"node_id\": \"MDQ6VXNlcjIxMDMxMDY3\", \"avatar_url\": \"https://avatars1.githubusercontent.com/u/21031067?v=4\", \"gravatar_id\": \"\", \"url\": \"https://api.github.com/users/Codertocat\", \"html_url\": \"https://github.com/Codertocat\", \"followers_url\": \"https://api.github.com/users/Codertocat/followers\", \"following_url\": \"https://api.github.com/users/Codertocat/following{/other_user}\", \"gists_url\": \"https://api.github.com/users/Codertocat/gists{/gist_id}\", \"starred_url\": \"https://api.github.com/users/Codertocat/starred{/owner}{/repo}\", \"subscriptions_url\": \"https://api.github.com/users/Codertocat/subscriptions\", \"organizations_url\": \"https://api.github.com/users/Codertocat/orgs\", \"repos_url\": \"https://api.github.com/users/Codertocat/repos\", \"events_url\": \"https://api.github.com/users/Codertocat/events{/privacy}\", \"received_events_url\": \"https://api.github.com/users/Codertocat/received_events\", \"type\": \"User\", \"site_admin\": false }, \"html_url\": \"https://github.com/Codertocat/Hello-World\", \"description\": null, \"fork\": false, \"url\": \"https://api.github.com/repos/Codertocat/Hello-World\", \"forks_url\": \"https://api.github.com/repos/Codertocat/Hello-World/forks\", \"keys_url\": \"https://api.github.com/repos/Codertocat/Hello-World/keys{/key_id}\", \"collaborators_url\": \"https://api.github.com/repos/Codertocat/Hello-World/collaborators{/collaborator}\", \"teams_url\": \"https://api.github.com/repos/Codertocat/Hello-World/teams\", \"hooks_url\": \"https://api.github.com/repos/Codertocat/Hello-World/hooks\", \"issue_events_url\": \"https://api.github.com/repos/Codertocat/Hello-World/issues/events{/number}\", \"events_url\": \"https://api.github.com/repos/Codertocat/Hello-World/events\", \"assignees_url\": \"https://api.github.com/repos/Codertocat/Hello-World/assignees{/user}\", \"branches_url\": \"https://api.github.com/repos/Codertocat/Hello-World/branches{/branch}\", \"tags_url\": \"https://api.github.com/repos/Codertocat/Hello-World/tags\", \"blobs_url\": \"https://api.github.com/repos/Codertocat/Hello-World/git/blobs{/sha}\", \"git_tags_url\": \"https://api.github.com/repos/Codertocat/Hello-World/git/tags{/sha}\", \"git_refs_url\": \"https://api.github.com/repos/Codertocat/Hello-World/git/refs{/sha}\", \"trees_url\": \"https://api.github.com/repos/Codertocat/Hello-World/git/trees{/sha}\", \"statuses_url\": \"https://api.github.com/repos/Codertocat/Hello-World/statuses/{sha}\", \"languages_url\": \"https://api.github.com/repos/Codertocat/Hello-World/languages\", \"stargazers_url\": \"https://api.github.com/repos/Codertocat/Hello-World/stargazers\", \"contributors_url\": \"https://api.github.com/repos/Codertocat/Hello-World/contributors\", \"subscribers_url\": \"https://api.github.com/repos/Codertocat/Hello-World/subscribers\", \"subscription_url\": \"https://api.github.com/repos/Codertocat/Hello-World/subscription\", \"commits_url\": \"https://api.github.com/repos/Codertocat/Hello-World/commits{/sha}\", \"git_commits_url\": \"https://api.github.com/repos/Codertocat/Hello-World/git/commits{/sha}\", \"comments_url\": \"https://api.github.com/repos/Codertocat/Hello-World/comments{/number}\", \"issue_comment_url\": \"https://api.github.com/repos/Codertocat/Hello-World/issues/comments{/number}\", \"contents_url\": \"https://api.github.com/repos/Codertocat/Hello-World/contents/{+path}\", \"compare_url\": \"https://api.github.com/repos/Codertocat/Hello-World/compare/{base}...{head}\", \"merges_url\": \"https://api.github.com/repos/Codertocat/Hello-World/merges\", \"archive_url\": \"https://api.github.com/repos/Codertocat/Hello-World/{archive_format}{/ref}\", \"downloads_url\": \"https://api.github.com/repos/Codertocat/Hello-World/downloads\", \"issues_url\": \"https://api.github.com/repos/Codertocat/Hello-World/issues{/number}\", \"pulls_url\": \"https://api.github.com/repos/Codertocat/Hello-World/pulls{/number}\", \"milestones_url\": \"https://api.github.com/repos/Codertocat/Hello-World/milestones{/number}\", \"notifications_url\": \"https://api.github.com/repos/Codertocat/Hello-World/notifications{?since,all,participating}\", \"labels_url\": \"https://api.github.com/repos/Codertocat/Hello-World/labels{/name}\", \"releases_url\": \"https://api.github.com/repos/Codertocat/Hello-World/releases{/id}\", \"deployments_url\": \"https://api.github.com/repos/Codertocat/Hello-World/deployments\", \"created_at\": \"2019-05-15T15:19:25Z\", \"updated_at\": \"2019-05-15T15:19:27Z\", \"pushed_at\": \"2019-05-15T15:20:32Z\", \"git_url\": \"git://github.com/Codertocat/Hello-World.git\", \"ssh_url\": \"git@github.com:Codertocat/Hello-World.git\", \"clone_url\": \"https://github.com/Codertocat/Hello-World.git\", \"svn_url\": \"https://github.com/Codertocat/Hello-World\", \"homepage\": null, \"size\": 0, \"stargazers_count\": 0, \"watchers_count\": 0, \"language\": null, \"has_issues\": true, \"has_projects\": true, \"has_downloads\": true, \"has_wiki\": true, \"has_pages\": true, \"forks_count\": 0, \"mirror_url\": null, \"archived\": false, \"disabled\": false, \"open_issues_count\": 2, \"license\": null, \"forks\": 0, \"open_issues\": 2, \"watchers\": 0, \"default_branch\": \"master\" } }, \"base\": { \"label\": \"Codertocat:master\", \"ref\": \"master\", \"sha\": \"f95f852bd8fca8fcc58a9a2d6c842781e32a215e\", \"user\": { \"login\": \"Codertocat\", \"id\": 21031067, \"node_id\": \"MDQ6VXNlcjIxMDMxMDY3\", \"avatar_url\": \"https://avatars1.githubusercontent.com/u/21031067?v=4\", \"gravatar_id\": \"\", \"url\": \"https://api.github.com/users/Codertocat\", \"html_url\": \"https://github.com/Codertocat\", \"followers_url\": \"https://api.github.com/users/Codertocat/followers\", \"following_url\": \"https://api.github.com/users/Codertocat/following{/other_user}\", \"gists_url\": \"https://api.github.com/users/Codertocat/gists{/gist_id}\", \"starred_url\": \"https://api.github.com/users/Codertocat/starred{/owner}{/repo}\", \"subscriptions_url\": \"https://api.github.com/users/Codertocat/subscriptions\", \"organizations_url\": \"https://api.github.com/users/Codertocat/orgs\", \"repos_url\": \"https://api.github.com/users/Codertocat/repos\", \"events_url\": \"https://api.github.com/users/Codertocat/events{/privacy}\", \"received_events_url\": \"https://api.github.com/users/Codertocat/received_events\", \"type\": \"User\", \"site_admin\": false }, \"repo\": { \"id\": 186853002, \"node_id\": \"MDEwOlJlcG9zaXRvcnkxODY4NTMwMDI=\", \"name\": \"Hello-World\", \"full_name\": \"Codertocat/Hello-World\", \"private\": false, \"owner\": { \"login\": \"Codertocat\", \"id\": 21031067, \"node_id\": \"MDQ6VXNlcjIxMDMxMDY3\", \"avatar_url\": \"https://avatars1.githubusercontent.com/u/21031067?v=4\", \"gravatar_id\": \"\", \"url\": \"https://api.github.com/users/Codertocat\", \"html_url\": \"https://github.com/Codertocat\", \"followers_url\": \"https://api.github.com/users/Codertocat/followers\", \"following_url\": \"https://api.github.com/users/Codertocat/following{/other_user}\", \"gists_url\": \"https://api.github.com/users/Codertocat/gists{/gist_id}\", \"starred_url\": \"https://api.github.com/users/Codertocat/starred{/owner}{/repo}\", \"subscriptions_url\": \"https://api.github.com/users/Codertocat/subscriptions\", \"organizations_url\": \"https://api.github.com/users/Codertocat/orgs\", \"repos_url\": \"https://api.github.com/users/Codertocat/repos\", \"events_url\": \"https://api.github.com/users/Codertocat/events{/privacy}\", \"received_events_url\": \"https://api.github.com/users/Codertocat/received_events\", \"type\": \"User\", \"site_admin\": false }, \"html_url\": \"https://github.com/Codertocat/Hello-World\", \"description\": null, \"fork\": false, \"url\": \"https://api.github.com/repos/Codertocat/Hello-World\", \"forks_url\": \"https://api.github.com/repos/Codertocat/Hello-World/forks\", \"keys_url\": \"https://api.github.com/repos/Codertocat/Hello-World/keys{/key_id}\", \"collaborators_url\": \"https://api.github.com/repos/Codertocat/Hello-World/collaborators{/collaborator}\", \"teams_url\": \"https://api.github.com/repos/Codertocat/Hello-World/teams\", \"hooks_url\": \"https://api.github.com/repos/Codertocat/Hello-World/hooks\", \"issue_events_url\": \"https://api.github.com/repos/Codertocat/Hello-World/issues/events{/number}\", \"events_url\": \"https://api.github.com/repos/Codertocat/Hello-World/events\", \"assignees_url\": \"https://api.github.com/repos/Codertocat/Hello-World/assignees{/user}\", \"branches_url\": \"https://api.github.com/repos/Codertocat/Hello-World/branches{/branch}\", \"tags_url\": \"https://api.github.com/repos/Codertocat/Hello-World/tags\", \"blobs_url\": \"https://api.github.com/repos/Codertocat/Hello-World/git/blobs{/sha}\", \"git_tags_url\": \"https://api.github.com/repos/Codertocat/Hello-World/git/tags{/sha}\", \"git_refs_url\": \"https://api.github.com/repos/Codertocat/Hello-World/git/refs{/sha}\", \"trees_url\": \"https://api.github.com/repos/Codertocat/Hello-World/git/trees{/sha}\", \"statuses_url\": \"https://api.github.com/repos/Codertocat/Hello-World/statuses/{sha}\", \"languages_url\": \"https://api.github.com/repos/Codertocat/Hello-World/languages\", \"stargazers_url\": \"https://api.github.com/repos/Codertocat/Hello-World/stargazers\", \"contributors_url\": \"https://api.github.com/repos/Codertocat/Hello-World/contributors\", \"subscribers_url\": \"https://api.github.com/repos/Codertocat/Hello-World/subscribers\", \"subscription_url\": \"https://api.github.com/repos/Codertocat/Hello-World/subscription\", \"commits_url\": \"https://api.github.com/repos/Codertocat/Hello-World/commits{/sha}\", \"git_commits_url\": \"https://api.github.com/repos/Codertocat/Hello-World/git/commits{/sha}\", \"comments_url\": \"https://api.github.com/repos/Codertocat/Hello-World/comments{/number}\", \"issue_comment_url\": \"https://api.github.com/repos/Codertocat/Hello-World/issues/comments{/number}\", \"contents_url\": \"https://api.github.com/repos/Codertocat/Hello-World/contents/{+path}\", \"compare_url\": \"https://api.github.com/repos/Codertocat/Hello-World/compare/{base}...{head}\", \"merges_url\": \"https://api.github.com/repos/Codertocat/Hello-World/merges\", \"archive_url\": \"https://api.github.com/repos/Codertocat/Hello-World/{archive_format}{/ref}\", \"downloads_url\": \"https://api.github.com/repos/Codertocat/Hello-World/downloads\", \"issues_url\": \"https://api.github.com/repos/Codertocat/Hello-World/issues{/number}\", \"pulls_url\": \"https://api.github.com/repos/Codertocat/Hello-World/pulls{/number}\", \"milestones_url\": \"https://api.github.com/repos/Codertocat/Hello-World/milestones{/number}\", \"notifications_url\": \"https://api.github.com/repos/Codertocat/Hello-World/notifications{?since,all,participating}\", \"labels_url\": \"https://api.github.com/repos/Codertocat/Hello-World/labels{/name}\", \"releases_url\": \"https://api.github.com/repos/Codertocat/Hello-World/releases{/id}\", \"deployments_url\": \"https://api.github.com/repos/Codertocat/Hello-World/deployments\", \"created_at\": \"2019-05-15T15:19:25Z\", \"updated_at\": \"2019-05-15T15:19:27Z\", \"pushed_at\": \"2019-05-15T15:20:32Z\", \"git_url\": \"git://github.com/Codertocat/Hello-World.git\", \"ssh_url\": \"git@github.com:Codertocat/Hello-World.git\", \"clone_url\": \"https://github.com/Codertocat/Hello-World.git\", \"svn_url\": \"https://github.com/Codertocat/Hello-World\", \"homepage\": null, \"size\": 0, \"stargazers_count\": 0, \"watchers_count\": 0, \"language\": null, \"has_issues\": true, \"has_projects\": true, \"has_downloads\": true, \"has_wiki\": true, \"has_pages\": true, \"forks_count\": 0, \"mirror_url\": null, \"archived\": false, \"disabled\": false, \"open_issues_count\": 2, \"license\": null, \"forks\": 0, \"open_issues\": 2, \"watchers\": 0, \"default_branch\": \"master\" } }, \"_links\": { \"self\": { \"href\": \"https://api.github.com/repos/Codertocat/Hello-World/pulls/2\" }, \"html\": { \"href\": \"https://github.com/Codertocat/Hello-World/pull/2\" }, \"issue\": { \"href\": \"https://api.github.com/repos/Codertocat/Hello-World/issues/2\" }, \"comments\": { \"href\": \"https://api.github.com/repos/Codertocat/Hello-World/issues/2/comments\" }, \"review_comments\": { \"href\": \"https://api.github.com/repos/Codertocat/Hello-World/pulls/2/comments\" }, \"review_comment\": { \"href\": \"https://api.github.com/repos/Codertocat/Hello-World/pulls/comments{/number}\" }, \"commits\": { \"href\": \"https://api.github.com/repos/Codertocat/Hello-World/pulls/2/commits\" }, \"statuses\": { \"href\": \"https://api.github.com/repos/Codertocat/Hello-World/statuses/ec26c3e57ca3a959ca5aad62de7213c562f8c821\" } }, \"author_association\": \"OWNER\", \"draft\": false, \"merged\": true, \"mergeable\": null, \"rebaseable\": null, \"mergeable_state\": \"unknown\", \"merged_by\": null, \"comments\": 0, \"review_comments\": 0, \"maintainer_can_modify\": false, \"commits\": 1, \"additions\": 1, \"deletions\": 1, \"changed_files\": 1 }, \"repository\": { \"id\": 186853002, \"node_id\": \"MDEwOlJlcG9zaXRvcnkxODY4NTMwMDI=\", \"name\": \"Hello-World\", \"full_name\": \"Codertocat/Hello-World\", \"private\": false, \"owner\": { \"login\": \"Codertocat\", \"id\": 21031067, \"node_id\": \"MDQ6VXNlcjIxMDMxMDY3\", \"avatar_url\": \"https://avatars1.githubusercontent.com/u/21031067?v=4\", \"gravatar_id\": \"\", \"url\": \"https://api.github.com/users/Codertocat\", \"html_url\": \"https://github.com/Codertocat\", \"followers_url\": \"https://api.github.com/users/Codertocat/followers\", \"following_url\": \"https://api.github.com/users/Codertocat/following{/other_user}\", \"gists_url\": \"https://api.github.com/users/Codertocat/gists{/gist_id}\", \"starred_url\": \"https://api.github.com/users/Codertocat/starred{/owner}{/repo}\", \"subscriptions_url\": \"https://api.github.com/users/Codertocat/subscriptions\", \"organizations_url\": \"https://api.github.com/users/Codertocat/orgs\", \"repos_url\": \"https://api.github.com/users/Codertocat/repos\", \"events_url\": \"https://api.github.com/users/Codertocat/events{/privacy}\", \"received_events_url\": \"https://api.github.com/users/Codertocat/received_events\", \"type\": \"User\", \"site_admin\": false }, \"html_url\": \"https://github.com/Codertocat/Hello-World\", \"description\": null, \"fork\": false, \"url\": \"https://api.github.com/repos/Codertocat/Hello-World\", \"forks_url\": \"https://api.github.com/repos/Codertocat/Hello-World/forks\", \"keys_url\": \"https://api.github.com/repos/Codertocat/Hello-World/keys{/key_id}\", \"collaborators_url\": \"https://api.github.com/repos/Codertocat/Hello-World/collaborators{/collaborator}\", \"teams_url\": \"https://api.github.com/repos/Codertocat/Hello-World/teams\", \"hooks_url\": \"https://api.github.com/repos/Codertocat/Hello-World/hooks\", \"issue_events_url\": \"https://api.github.com/repos/Codertocat/Hello-World/issues/events{/number}\", \"events_url\": \"https://api.github.com/repos/Codertocat/Hello-World/events\", \"assignees_url\": \"https://api.github.com/repos/Codertocat/Hello-World/assignees{/user}\", \"branches_url\": \"https://api.github.com/repos/Codertocat/Hello-World/branches{/branch}\", \"tags_url\": \"https://api.github.com/repos/Codertocat/Hello-World/tags\", \"blobs_url\": \"https://api.github.com/repos/Codertocat/Hello-World/git/blobs{/sha}\", \"git_tags_url\": \"https://api.github.com/repos/Codertocat/Hello-World/git/tags{/sha}\", \"git_refs_url\": \"https://api.github.com/repos/Codertocat/Hello-World/git/refs{/sha}\", \"trees_url\": \"https://api.github.com/repos/Codertocat/Hello-World/git/trees{/sha}\", \"statuses_url\": \"https://api.github.com/repos/Codertocat/Hello-World/statuses/{sha}\", \"languages_url\": \"https://api.github.com/repos/Codertocat/Hello-World/languages\", \"stargazers_url\": \"https://api.github.com/repos/Codertocat/Hello-World/stargazers\", \"contributors_url\": \"https://api.github.com/repos/Codertocat/Hello-World/contributors\", \"subscribers_url\": \"https://api.github.com/repos/Codertocat/Hello-World/subscribers\", \"subscription_url\": \"https://api.github.com/repos/Codertocat/Hello-World/subscription\", \"commits_url\": \"https://api.github.com/repos/Codertocat/Hello-World/commits{/sha}\", \"git_commits_url\": \"https://api.github.com/repos/Codertocat/Hello-World/git/commits{/sha}\", \"comments_url\": \"https://api.github.com/repos/Codertocat/Hello-World/comments{/number}\", \"issue_comment_url\": \"https://api.github.com/repos/Codertocat/Hello-World/issues/comments{/number}\", \"contents_url\": \"https://api.github.com/repos/Codertocat/Hello-World/contents/{+path}\", \"compare_url\": \"https://api.github.com/repos/Codertocat/Hello-World/compare/{base}...{head}\", \"merges_url\": \"https://api.github.com/repos/Codertocat/Hello-World/merges\", \"archive_url\": \"https://api.github.com/repos/Codertocat/Hello-World/{archive_format}{/ref}\", \"downloads_url\": \"https://api.github.com/repos/Codertocat/Hello-World/downloads\", \"issues_url\": \"https://api.github.com/repos/Codertocat/Hello-World/issues{/number}\", \"pulls_url\": \"https://api.github.com/repos/Codertocat/Hello-World/pulls{/number}\", \"milestones_url\": \"https://api.github.com/repos/Codertocat/Hello-World/milestones{/number}\", \"notifications_url\": \"https://api.github.com/repos/Codertocat/Hello-World/notifications{?since,all,participating}\", \"labels_url\": \"https://api.github.com/repos/Codertocat/Hello-World/labels{/name}\", \"releases_url\": \"https://api.github.com/repos/Codertocat/Hello-World/releases{/id}\", \"deployments_url\": \"https://api.github.com/repos/Codertocat/Hello-World/deployments\", \"created_at\": \"2019-05-15T15:19:25Z\", \"updated_at\": \"2019-05-15T15:19:27Z\", \"pushed_at\": \"2019-05-15T15:20:32Z\", \"git_url\": \"git://github.com/Codertocat/Hello-World.git\", \"ssh_url\": \"git@github.com:Codertocat/Hello-World.git\", \"clone_url\": \"https://github.com/Codertocat/Hello-World.git\", \"svn_url\": \"https://github.com/Codertocat/Hello-World\", \"homepage\": null, \"size\": 0, \"stargazers_count\": 0, \"watchers_count\": 0, \"language\": null, \"has_issues\": true, \"has_projects\": true, \"has_downloads\": true, \"has_wiki\": true, \"has_pages\": true, \"forks_count\": 0, \"mirror_url\": null, \"archived\": false, \"disabled\": false, \"open_issues_count\": 2, \"license\": null, \"forks\": 0, \"open_issues\": 2, \"watchers\": 0, \"default_branch\": \"master\" }, \"sender\": { \"login\": \"ebenezergraham\", \"id\": 21031067, \"node_id\": \"MDQ6VXNlcjIxMDMxMDY3\", \"avatar_url\": \"https://avatars1.githubusercontent.com/u/21031067?v=4\", \"gravatar_id\": \"\", \"url\": \"https://api.github.com/users/Codertocat\", \"html_url\": \"https://github.com/Codertocat\", \"followers_url\": \"https://api.github.com/users/Codertocat/followers\", \"following_url\": \"https://api.github.com/users/Codertocat/following{/other_user}\", \"gists_url\": \"https://api.github.com/users/Codertocat/gists{/gist_id}\", \"starred_url\": \"https://api.github.com/users/Codertocat/starred{/owner}{/repo}\", \"subscriptions_url\": \"https://api.github.com/users/Codertocat/subscriptions\", \"organizations_url\": \"https://api.github.com/users/Codertocat/orgs\", \"repos_url\": \"https://api.github.com/users/Codertocat/repos\", \"events_url\": \"https://api.github.com/users/Codertocat/events{/privacy}\", \"received_events_url\": \"https://api.github.com/users/Codertocat/received_events\", \"type\": \"User\", \"site_admin\": false } }";

	@After
	public void tearDown() {
		rewardRepository.deleteById(rewardRepository.findRewardByIssueId(testIssue).get().getId());
		Optional<User> user = userRepository.findByUsername("ebenezergraham");
		userRepository.deleteAll();
		userRepository.save(user.get());
		allocatedIssueRepository.delete(allocatedIssueRepository.findIssueByUrl(testIssue).get());
	}

	@Test
	public void shouldRewardContributor() {
		User sampleOAuth2User = new User();
		sampleOAuth2User.setName("Hermes Ananse");
		sampleOAuth2User.setEmail("hermes@openmeed.com");
		sampleOAuth2User.setUsername(testUser);
		sampleOAuth2User.setProvider(AuthProvider.github);

		userRepository.save(sampleOAuth2User);
		String rewardValue = "1000";
		// Allocate incentive to issueEntity
		Reward reward = new Reward();
		reward.setIssueId(testIssue);
		reward.setValue(rewardValue);
		reward.setType("pts");
		Reward res = rewardRepository.save(reward);
		assertNotNull(res);
		assertEquals(res.getValue(), rewardValue);

		//Simulate user selecting issueEntity
		Issue issue = new Issue();
    issue.setUrl(testIssue);
    issue.getAssignee().setLogin(testUser);
		Issue issueEntityResult = allocatedIssueRepository.save(issue);
		assertNotNull(issueEntityResult);

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		JSONObject payloadJsonObject = new JSONObject();
		JSONObject prJsonObject = new JSONObject();
		prJsonObject.put("merged",true);
		prJsonObject.put("issue_url",testIssue);
		JSONObject senderJsonObject = new JSONObject();
		senderJsonObject.put("login","hermes");
		payloadJsonObject.put("pull_request",prJsonObject);
		payloadJsonObject.put("sender",senderJsonObject);
		payloadJsonObject.put("action","closed");

		HttpEntity<String> request = new HttpEntity<String>(payloadJsonObject.toString(), headers);
		ResponseEntity<String> response = restTemplate.postForEntity(githubEventEndpoint, request,String.class);

		Optional<User> user = userRepository.findByUsername(testUser);
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		assertEquals(user.get().getPoints(), rewardValue);

		// The incentive should exist after it has been transferred to the contributor
		Optional<Reward> redeemReward = rewardRepository.findRewardByIssueId(issueEntityResult.getUrl());
		System.out.println(redeemReward.get().getValue());
		assertNull(redeemReward.get());

	}

	@Test
	public void shouldNotRewardRejectedContribution() {
		User sampleOAuth2User = new User();
		sampleOAuth2User.setName("Nana Ananse");
		sampleOAuth2User.setEmail("ananse@openmeed.com");
		sampleOAuth2User.setUsername("ananse");
		sampleOAuth2User.setProvider(AuthProvider.github);

		userRepository.save(sampleOAuth2User);
		String rewardValue = "1000";
		// Allocate incentive to issueEntity
		Reward reward = new Reward();
		reward.setIssueId(testIssue);
		reward.setValue(rewardValue);
		reward.setType("pts");
		Reward res = rewardRepository.save(reward);
		assertNotNull(res);
		assertEquals(res.getValue(), rewardValue);

    //Simulate user selecting issueEntity
    Issue issue = new Issue();
    issue.setUrl(testIssue);
    issue.getAssignee().setLogin(testUser);
    Issue issueEntityResult = allocatedIssueRepository.save(issue);
		assertNotNull(issueEntityResult);

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		JSONObject payloadJsonObject = new JSONObject();
		JSONObject prJsonObject = new JSONObject();
		prJsonObject.put("merged",false); // Meaning solution wasn't accepted into the codebase
		prJsonObject.put("issue_url",testIssue);
		JSONObject senderJsonObject = new JSONObject();
		senderJsonObject.put("login","ananse");
		payloadJsonObject.put("pull_request",prJsonObject);
		payloadJsonObject.put("sender",senderJsonObject);
		payloadJsonObject.put("action","closed");

		HttpEntity<String> request = new HttpEntity<String>(payloadJsonObject.toString(), headers);
		ResponseEntity<String> response = restTemplate.postForEntity(githubEventEndpoint, request,String.class);

		Optional<User> user = userRepository.findByUsername("ananse");
		assertEquals(response.getStatusCode(), HttpStatus.OK);
		assertEquals(user.get().getPoints(), 0);

	}

	@Test
	public void verifyIncentiveAccuracy() {
		String rewardValue = "1000";

    //Simulate user selecting issueEntity
    Issue issue = new Issue();
    issue.setUrl(testIssue);
    issue.getAssignee().setLogin(testUser);
    Issue issueEntityResult = allocatedIssueRepository.save(issue);

		// Assign incentive to issueEntity
		Reward reward = new Reward();
		reward.setIssueId(issueEntityResult.getUrl());
		reward.setValue(rewardValue);
		reward.setType("pts");
		incentiveService.storeIncentive(reward);

		Optional<Reward> result = rewardRepository.findRewardByIssueId(issueEntityResult.getUrl());
		assertNotNull(result);
		assertEquals(result.get().getValue(),rewardValue);
	}

}
*/
