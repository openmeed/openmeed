package me.ebenezergraham.honours.platform.configuration;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class SeleniumConfig {

  private WebDriver driver;

  public SeleniumConfig() {

    FirefoxBinary firefoxBinary = new FirefoxBinary();
    //firefoxBinary.addCommandLineOptions("--headless");
    //firefoxBinary.addCommandLineOptions("--no-sandbox");
    //System.setProperty("webdriver.gecko.driver", "geckodriver");
    FirefoxOptions firefoxOptions = new FirefoxOptions();
    firefoxOptions.setBinary(firefoxBinary);
    driver = new FirefoxDriver(firefoxOptions);
    driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
  }

  static {
    System.setProperty("webdriver.gecko.driver", findFile("geckodriver"));
  }

  static private String findFile(String filename) {
    String paths[] = {"", "bin/", "target/classes"};
    for (String path : paths) {
      if (new File(path + filename).exists())
        return path + filename;
    }
    return "";
  }

  public void clickElement(WebElement element) {
    element.click();
  }

  public WebDriver getDriver() {
    return driver;
  }

  public void navigateTo(String url) {
    driver.navigate().to(url);
  }

}
