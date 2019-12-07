package me.ebenezergraham.honours.platform;

import org.apache.tomcat.util.net.SSLContext;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PlatformApplicationTests {
	
	private static final String WELCOME_URL = "https://localhost:8443/login";
	
	@Value("${trust.store}")
	private Resource trustStore;
	
	@Value("${trust.store.password}")
	private String trustStorePassword;

	@Test
	public void contextLoads() {
	}

	@BeforeClass
	public static void setUp() {
		seleniumExample = new SeleniumTest();
	}

	private static SeleniumTest seleniumExample;
	private String expectedTitle = "Baeldung | Java, Spring and Web Development tutorials";

	@AfterClass
	public static void tearDown() {
		seleniumExample.closeWindow();
	}

	@Test
	public void whenAboutBaeldungIsLoaded_thenAboutEugenIsMentionedOnPage() {
		seleniumExample.getAboutBaeldungPage();
		String actualTitle = seleniumExample.getTitle();

		assertNotNull(actualTitle);
		assertEquals(expectedTitle, actualTitle);
		assertTrue(seleniumExample.isAuthorInformationAvailable());
	}
}
