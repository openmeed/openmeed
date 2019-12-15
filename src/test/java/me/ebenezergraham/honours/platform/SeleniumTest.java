/*
package me.ebenezergraham.honours.platform;

import me.ebenezergraham.honours.platform.configuration.SeleniumConfig;
import net.minidev.json.JSONObject;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SeleniumTest {

  private SeleniumConfig config;
  private String url = "https://openmeed.netlify.com/login";

  public SeleniumTest() {
    config = new SeleniumConfig();
    config.getDriver().get(url);
  }

  @After
  public void closeWindow() {
    this.config.getDriver().close();
  }

  public String getTitle() {
    return this.config.getDriver().getTitle();
  }

  boolean firstRun = true;

  public void createIssue(Date now){
    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth("53e3bf575ba728eb99a3189b84a590241539aff9");
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("title",now.getTime());
    HttpEntity<String> request = new HttpEntity<>(jsonObject.toString(), headers);
    ResponseEntity<String> response = restTemplate.postForEntity("https://api.github.com/repos/ebenezergraham/test/issues", request,String.class);
    assertEquals(response.getStatusCode(), HttpStatus.CREATED);

  }


  public void createPR(){
    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth("53e3bf575ba728eb99a3189b84a590241539aff9");
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("title","Test Issue");
    HttpEntity<String> request = new HttpEntity<>(jsonObject.toString(), headers);
    ResponseEntity<String> response = restTemplate.postForEntity("https://api.github.com/repos/ebenezergraham/test/issues", request,String.class);
    assertEquals(response.getStatusCode(), HttpStatus.CREATED);
  }

  public void mergePR(){
    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setBearerAuth("53e3bf575ba728eb99a3189b84a590241539aff9");
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("title","Test Issue");
    HttpEntity<String> request = new HttpEntity<>(jsonObject.toString(), headers);
    ResponseEntity<String> response = restTemplate.postForEntity("https://api.github.com/repos/ebenezergraham/test/issues", request,String.class);
    assertEquals(response.getStatusCode(), HttpStatus.CREATED);
  }

  @Test
  public void performTypicalUserSurf() throws InterruptedException {
    Date now = new Date();
    // 2 hours
    // Date expiryDate = new Date(new Date().getTime()+3699990);
    Date expiryDate = new Date(new Date().getTime() + 240000);
    while (now.before(expiryDate)) {
      this.config.getDriver().findElement(By.id("login")).click();

      if (firstRun) {
        firstRun = false;
        this.config.getDriver().findElement(By.id("login_field")).sendKeys("ebenezergraham");
        this.config.getDriver().findElement(By.id("password")).sendKeys("kwesiakyen 96317");
        this.config.getDriver().findElement(By.className("btn")).click();
      }

      this.config.getDriver().findElement(By.id("admin")).click();
      this.config.getDriver().findElement(By.id("test")).click();
      this.config.getDriver().findElement(By.id("activate-repositories-btn")).click();
      this.config.getDriver().findElement(By.id("profile")).click();
      this.config.getDriver().findElement(By.id("dashboard")).click();
      Thread.sleep(3000);
      WebElement issue = this.config.getDriver()
          .findElement(By.cssSelector("body > openmeed-root > openmeed-dashboard > div > div:nth-child(2) > div:nth-child(3) > div:nth-child(1) > button > a"));
      issue.isDisplayed();
      issue.click();
      createIssue(now);
      createPR();
      this.config.getDriver().findElement(By.id("leaderboard")).click();
      Thread.sleep(3000);


      this.config.getDriver().findElement(By.id("logout")).click();
      Thread.sleep(3000);
    }
  }
}

*/
