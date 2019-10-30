package me.ebenezergraham.honours.platform;

import org.apache.tomcat.util.net.SSLContext;
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

/*@RunWith(SpringRunner.class)
@SpringBootTest*/
public class PlatformApplicationTests {
	
	private static final String WELCOME_URL = "https://localhost:8443/login";
	
	@Value("${trust.store}")
	private Resource trustStore;
	
	@Value("${trust.store.password}")
	private String trustStorePassword;

	@Test
	public void contextLoads() {
	}

}
