
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

	@After
	public void tearDown() {
		rewardRepository.deleteById(rewardRepository.findRewardByIssueId(testIssue).get().getId());
		Optional<User> user = userRepository.findByUsername("ebenezergraham");
		userRepository.deleteAll();
		userRepository.save(user.get());
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
		Reward res = rewardRepository.save(reward);
		assertNotNull(res);
		assertEquals(res.getValue(), rewardValue);

		//Simulate user selecting issueEntity
		Issue issue = new Issue();
    issue.setUrl(testIssue);
    issue.setAssigneeName(testUser);
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

		HttpEntity<String> request = new HttpEntity<>(payloadJsonObject.toString(), headers);
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
    issue.setAssigneeName(testUser);
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
    issue.setAssigneeName(testUser);
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

