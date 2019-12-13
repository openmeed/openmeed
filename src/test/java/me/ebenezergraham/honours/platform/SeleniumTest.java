package me.ebenezergraham.honours.platform;

import me.ebenezergraham.honours.platform.configuration.SeleniumConfig;
import org.eclipse.persistence.jpa.jpql.parser.DateTime;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

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
    //  this.config.getDriver().close();
  }

  public String getTitle() {
    return this.config.getDriver().getTitle();
  }

/*  @Test
  public void shouldLoginCheckIssuesAndVisitProfile() {
    assertEquals(getTitle(),"Webapp");
    clickSso();
    assertTrue(isIssuesAvailable());
    visitProfile();
  }*/

  private void clickSso() {
    //this.config.getDriver().findElement(By.id("login")).click();
    this.config.getDriver()
        .findElement(By.cssSelector(".btn"))
        .click();
  }

  private void visitProfile() {
    Actions builder = new Actions(config.getDriver());
    WebElement element = this.config.getDriver()
        .findElement(By.partialLinkText("Profile"));
    builder.moveToElement(element)
        .build()
        .perform();
  }

  boolean firstRun = true;

  @Test
  public void performTypicalUserSurf() throws InterruptedException {
/*
    Actions builder = new Actions(config.getDriver());
    WebElement element = this.config.getDriver().findElement(By.id("profile"));
    WebElement profile = this.config.getDriver().findElement(By.id("profile"));
    WebElement dashboard = this.config.getDriver().findElement(By.id("dashboard"));
    WebElement admin = this.config.getDriver().findElement(By.id("admin"));
    WebElement leaderboard = this.config.getDriver().findElement(By.id("leaderboard"));
    WebElement logout = this.config.getDriver().findElement(By.id("logout"));
    WebElement login = this.config.getDriver().findElement(By.id("login"));
    WebElement repo = this.config.getDriver().findElement(By.cssSelector("personalized-tab"));
    WebElement activateRepoBtn = this.config.getDriver().findElement(By.id("activate-repositories-btn"));
    WebElement issueLink = this.config.getDriver().findElement(By.cssSelector("body > openmeed-root > openmeed-dashboard > div > div:nth-child(2) > div:nth-child(3) > div:nth-child(1) > button > a"));
*/

//    WebElement issueLink = this.config.getDriver().findElement(By.cssSelector("body > openmeed-root > openmeed-dashboard > div > div:nth-child(2) > div:nth-child(3) > div:nth-child(1) > button > a"));

//    builder
//        .moveToElement(login).click()
//        .moveToElement(admin).click()
//        .moveToElement(repo).click()
//        .moveToElement(activateRepoBtn).click()
//        .moveToElement(dashboard).click()
//        .moveToElement(profile).click()
//        .moveToElement(leaderboard).click()
//        .moveToElement(logout).click()
//        .build().perform();
    Date now = new Date();
    // Date expiryDate = new Date(new Date().getTime()+3699990);
    Date expiryDate = new Date(new Date().getTime() + 120000);
    while (now.before(expiryDate)) {
      this.config.getDriver().findElement(By.id("login")).click();

      if (firstRun) {
        firstRun = false;
        System.out.println(firstRun);
        this.config.getDriver().findElement(By.id("login_field")).sendKeys("ebenezergraham");
        this.config.getDriver().findElement(By.id("password")).sendKeys("kwesiakyen 96317");
        this.config.getDriver().findElement(By.className("btn")).click();
      }

      this.config.getDriver().findElement(By.id("admin")).click();
      this.config.getDriver().findElement(By.id("personalized-tab")).click();
      this.config.getDriver().findElement(By.id("activate-repositories-btn")).click();
      this.config.getDriver().findElement(By.id("profile")).click();
      this.config.getDriver().findElement(By.id("dashboard")).click();
      this.config.getDriver().findElement(By.id("leaderboard")).click();
      this.config.getDriver().findElement(By.id("logout")).click();
      Thread.sleep(5000);
    }

  }

  public boolean isIssuesAvailable() {
    return this.config.getDriver()
        .findElement(By.cssSelector("body > openmeed-root > openmeed-dashboard > div > div:nth-child(2) > div:nth-child(3) > div:nth-child(1) > button > a"))
        .isDisplayed();
  }
}

