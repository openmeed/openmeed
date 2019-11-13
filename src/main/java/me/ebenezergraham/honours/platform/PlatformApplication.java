package me.ebenezergraham.honours.platform;

import me.ebenezergraham.honours.platform.configuration.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 @author Ebenezer Graham
 Created on 9/30/19
 */
@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class PlatformApplication {
	public static void main(String[] args) {
		SpringApplication.run(PlatformApplication.class, args);
	}
}
