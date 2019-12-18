
package me.ebenezergraham.honours.platform;

import com.google.gson.Gson;
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
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.*;

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

  Gson gson = new Gson();

  @BeforeClass
  public static void setUp() {

  }

  private String testIssue = "https://github.com/openmeed/rewarder/issues/20";
  private String githubEventEndpoint = "http://localhost:/api/v1/github/events";
  private String testUser = "hermes";

  @After
  public void tearDown() {
    rewardRepository.deleteAll();
    userRepository.deleteAll();
    allocatedIssueRepository.deleteAll();
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
    ArrayList<String> authorities = new ArrayList<>();
    authorities.add("ebenezergraham");
    reward.setAuthorizer(authorities);
    Reward res = rewardRepository.save(reward);
    assertNotNull(res);
    assertEquals(res.getValue(), rewardValue);

    //Simulate user selecting issueEntity
    Issue issue = new Issue();
    issue.setUrl(testIssue);
    issue.setAssigneeName(testUser);
    Issue issueEntityResult = allocatedIssueRepository.save(issue);
    assertNotNull(issueEntityResult);

    JSONObject payloadJsonObject = new JSONObject();
    JSONObject prJsonObject = new JSONObject();
    prJsonObject.put("merged", true);
    prJsonObject.put("issue_url", testIssue);
    prJsonObject.put("assignee", "hermes");
    JSONObject senderJsonObject = new JSONObject();
    senderJsonObject.put("login", "hermes");
    payloadJsonObject.put("pull_request", prJsonObject);
    prJsonObject.put("requested_reviewers", new String[]{"ebenezergraham"});
    payloadJsonObject.put("sender", senderJsonObject);
    payloadJsonObject.put("action", "closed");

    rewardEngine.process(gson.fromJson(payloadJsonObject.toJSONString(), Payload.class));

    Optional<User> user = userRepository.findByUsername(testUser);

    assertEquals(user.get().getPoints(), Integer.parseInt(rewardValue));

    // The incentive should exist after it has been transferred to the contributor
    Optional<Reward> redeemReward = rewardRepository.findRewardByIssueId(issueEntityResult.getUrl());
    assertFalse(redeemReward.isPresent());
  }

  @Test
  public void shouldNotRewardRejectedContribution() {
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
    ArrayList<String> authorities = new ArrayList<>();
    authorities.add("ebenezergraham");
    reward.setAuthorizer(authorities);
    Reward res = rewardRepository.save(reward);
    assertEquals(res.getValue(), rewardValue);

    //Simulate user selecting issueEntity
    Issue issue = new Issue();
    issue.setUrl(testIssue);
    issue.setAssigneeName(testUser);
    Issue issueEntityResult = allocatedIssueRepository.save(issue);

    JSONObject payloadJsonObject = new JSONObject();
    JSONObject prJsonObject = new JSONObject();
    prJsonObject.put("merged", false);
    prJsonObject.put("issue_url", testIssue);
    prJsonObject.put("assignee", "seshat");
    JSONObject senderJsonObject = new JSONObject();
    senderJsonObject.put("login", "seshat");
    payloadJsonObject.put("pull_request", prJsonObject);
    payloadJsonObject.put("sender", senderJsonObject);
    payloadJsonObject.put("action", "closed");

    rewardEngine.process(gson.fromJson(payloadJsonObject.toJSONString(), Payload.class));

    Optional<User> user = userRepository.findByUsername(testUser);

    assertEquals(user.get().getPoints(), 0);

    // The incentive should exist after it has been transferred to the contributor
    Optional<Reward> redeemReward = rewardRepository.findRewardByIssueId(issueEntityResult.getUrl());
    assertTrue(redeemReward.isPresent());
  }

  @Test
  public void verifyIncentiveAccuracy() {
    String rewardValue = "1000";

    //Simulate user selecting issueEntity
    Issue issue = new Issue();
    issue.setUrl(testIssue);
    issue.setAssigneeName(testUser);
    Issue issueEntityResult = allocatedIssueRepository.save(issue);

    // Assign incentive to issueEntity
    Reward reward = new Reward();
    reward.setIssueId(issueEntityResult.getUrl());
    ArrayList<String> authorities = new ArrayList<>();
    authorities.add("ebenezergraham");
    reward.setAuthorizer(authorities);
    reward.setValue(rewardValue);
    reward.setType("pts");
    incentiveService.storeIncentive(reward);

    Optional<Reward> result = rewardRepository.findRewardByIssueId(issueEntityResult.getUrl());
    assertNotNull(result);
    assertEquals(result.get().getValue(), rewardValue);
  }

  @Test
  public void shouldNotRewardIfAuthorityIsTheSameAsContributor() {
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
    ArrayList<String> authorities = new ArrayList<>();
    authorities.add("ebenezergraham");
    reward.setAuthorizer(authorities);
    Reward res = rewardRepository.save(reward);
    assertNotNull(res);
    assertEquals(res.getValue(), rewardValue);

    //Simulate user selecting issueEntity
    Issue issue = new Issue();
    issue.setUrl(testIssue);
    issue.setAssigneeName(testUser);
    Issue issueEntityResult = allocatedIssueRepository.save(issue);
    assertNotNull(issueEntityResult);

    JSONObject payloadJsonObject = new JSONObject();
    JSONObject prJsonObject = new JSONObject();
    prJsonObject.put("merged", true);
    prJsonObject.put("issue_url", testIssue);
    prJsonObject.put("assignee", "ebenezergraham");
    prJsonObject.put("requested_reviewers", new String[]{"ebenezergraham"});

    JSONObject senderJsonObject = new JSONObject();
    senderJsonObject.put("login", "ebenezergraham");
    payloadJsonObject.put("pull_request", prJsonObject);
    payloadJsonObject.put("sender", senderJsonObject);
    payloadJsonObject.put("action", "closed");

    rewardEngine.process(gson.fromJson(payloadJsonObject.toJSONString(), Payload.class));

    Optional<User> user = userRepository.findByUsername(testUser);

    assertEquals(user.get().getPoints(), 0);

    // The incentive should exist after it has been transferred to the contributor
    Optional<Reward> redeemReward = rewardRepository.findRewardByIssueId(issueEntityResult.getUrl());
    assertTrue(redeemReward.isPresent());
  }

  @Test
  public void authoritiesMustBePartOfReviewers() {
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
    ArrayList<String> authorities = new ArrayList<>();
    authorities.add("ebenezergraham");
    reward.setAuthorizer(authorities);
    Reward res = rewardRepository.save(reward);
    assertNotNull(res);
    assertEquals(res.getValue(), rewardValue);

    //Simulate user selecting issueEntity
    Issue issue = new Issue();
    issue.setUrl(testIssue);
    issue.setAssigneeName(testUser);
    Issue issueEntityResult = allocatedIssueRepository.save(issue);
    assertNotNull(issueEntityResult);

    JSONObject payloadJsonObject = new JSONObject();
    JSONObject prJsonObject = new JSONObject();
    prJsonObject.put("merged", true);
    prJsonObject.put("issue_url", testIssue);
    prJsonObject.put("assignee", "seshat");
    prJsonObject.put("requested_reviewers", new String[]{"no_reviewer"});
    JSONObject senderJsonObject = new JSONObject();
    senderJsonObject.put("login", "seshat");
    payloadJsonObject.put("pull_request", prJsonObject);
    payloadJsonObject.put("sender", senderJsonObject);
    payloadJsonObject.put("action", "closed");

    rewardEngine.process(gson.fromJson(payloadJsonObject.toJSONString(), Payload.class));

    Optional<User> user = userRepository.findByUsername(testUser);

    assertEquals(user.get().getPoints(), 0);

    // The incentive should exist after it has been transferred to the contributor
    Optional<Reward> redeemReward = rewardRepository.findRewardByIssueId(issueEntityResult.getUrl());
    assertTrue(redeemReward.isPresent());
  }

  @Test
  public void issueAssigneeAndSolutionContributorMismatch() {
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
    ArrayList<String> authorities = new ArrayList<>();
    authorities.add("ebenezergraham");
    reward.setAuthorizer(authorities);
    reward.setType("pts");
    Reward res = rewardRepository.save(reward);
    assertNotNull(res);
    assertEquals(res.getValue(), rewardValue);

    //Simulate user selecting issueEntity
    Issue issue = new Issue();
    issue.setUrl(testIssue);
    issue.setAssigneeName(testUser);
    Issue issueEntityResult = allocatedIssueRepository.save(issue);
    assertNotNull(issueEntityResult);

    JSONObject payloadJsonObject = new JSONObject();
    JSONObject prJsonObject = new JSONObject();
    prJsonObject.put("merged", true);
    prJsonObject.put("issue_url", testIssue);
    JSONObject senderJsonObject = new JSONObject();
    senderJsonObject.put("login", "hermes");
    payloadJsonObject.put("pull_request", prJsonObject);
    payloadJsonObject.put("sender", senderJsonObject);
    payloadJsonObject.put("action", "closed");

    rewardEngine.process(gson.fromJson(payloadJsonObject.toJSONString(), Payload.class));

    Optional<User> user = userRepository.findByUsername(testUser);

    assertEquals(user.get().getPoints(), 0);

    // The incentive should exist after it has been transferred to the contributor
    Optional<Reward> redeemReward = rewardRepository.findRewardByIssueId(issueEntityResult.getUrl());
    assertTrue(redeemReward.isPresent());
  }


  @Test
  public void issueIsNotIncentivized() {
    User sampleOAuth2User = new User();
    sampleOAuth2User.setName("Hermes Ananse");
    sampleOAuth2User.setEmail("hermes@openmeed.com");
    sampleOAuth2User.setUsername(testUser);
    sampleOAuth2User.setProvider(AuthProvider.github);

    userRepository.save(sampleOAuth2User);

    JSONObject payloadJsonObject = new JSONObject();
    JSONObject prJsonObject = new JSONObject();
    prJsonObject.put("merged", true);
    prJsonObject.put("issue_url", testIssue);
    JSONObject senderJsonObject = new JSONObject();
    senderJsonObject.put("login", "hermes");
    payloadJsonObject.put("pull_request", prJsonObject);
    payloadJsonObject.put("sender", senderJsonObject);
    payloadJsonObject.put("action", "closed");

    rewardEngine.process(gson.fromJson(payloadJsonObject.toJSONString(), Payload.class));

    Optional<User> user = userRepository.findByUsername(testUser);

    assertEquals(user.get().getPoints(), 0);
  }
}

