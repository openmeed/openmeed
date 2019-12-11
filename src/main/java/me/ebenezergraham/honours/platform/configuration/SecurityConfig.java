package me.ebenezergraham.honours.platform.configuration;

import me.ebenezergraham.honours.platform.security.CustomUserDetailsService;
import me.ebenezergraham.honours.platform.security.RestAuthenticationEntryPoint;
import me.ebenezergraham.honours.platform.security.TokenAuthenticationFilter;
import me.ebenezergraham.honours.platform.security.oauth2.CustomOAuth2UserService;
import me.ebenezergraham.honours.platform.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import me.ebenezergraham.honours.platform.security.oauth2.OAuth2AuthenticationFailureHandler;
import me.ebenezergraham.honours.platform.security.oauth2.OAuth2AuthenticationSuccessHandler;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;

/**
 * @author Ebenezer Graham
 * Created on 9/30/19
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
    securedEnabled = true,
    jsr250Enabled = true,
    prePostEnabled = true
)
public class SecurityConfig extends WebSecurityConfigurerAdapter {


  @Value("${trust.store}")
  private Resource trustStore;

  @Value("${trust.store.password}")
  private String trustStorePassword;

  @Autowired
  private CustomUserDetailsService customUserDetailsService;

  @Autowired
  private CustomOAuth2UserService customOAuth2UserService;

  @Autowired
  private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

  @Autowired
  private OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

  @Autowired
  private HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

  @Bean
  public TokenAuthenticationFilter tokenAuthenticationFilter() {
    return new TokenAuthenticationFilter();
  }

  /*
    By default, Spring OAuth2 uses HttpSessionOAuth2AuthorizationRequestRepository to save
    the authorization request. But, since our service is stateless, we can't save it in
    the session. We'll save the request in a Base64 encoded cookie instead.
  */
  @Bean
  public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
    return new HttpCookieOAuth2AuthorizationRequestRepository();
  }

  @Override
  public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
    authenticationManagerBuilder
        .userDetailsService(customUserDetailsService)
        .passwordEncoder(passwordEncoder());
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }


  @Bean(BeanIds.AUTHENTICATION_MANAGER)
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.cors().and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
        .and()
        .csrf()
        .disable()
        .formLogin()
        .disable()
        .requiresChannel()
        .anyRequest().requiresInsecure()
        .and()
        .httpBasic()
        .disable()
        .exceptionHandling()
        .authenticationEntryPoint(new RestAuthenticationEntryPoint())
        .and()
        .authorizeRequests()
        .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
        .antMatchers("/", "/login**", "/login/oauth2/**").permitAll()
        .antMatchers("/api/v1/github/events").permitAll()
        .antMatchers("/api/v2**").permitAll()
        .antMatchers("/swagger-ui.html**").permitAll()
        .antMatchers("/h2-console**").permitAll()
        .antMatchers("/auth/**", "/oauth2/**").permitAll()
        .anyRequest()
        .authenticated()
        .and()
        .oauth2Login()
        .authorizationEndpoint()
        .baseUri("/oauth2/authorize")
        .authorizationRequestRepository(cookieAuthorizationRequestRepository())
        .and()
        .redirectionEndpoint()
        .baseUri("/oauth2/callback/*")
        .and()
        .userInfoEndpoint()
        .userService(customOAuth2UserService)
        .and()
        .successHandler(oAuth2AuthenticationSuccessHandler)
        .failureHandler(oAuth2AuthenticationFailureHandler);

    // Add our custom Token based authentication filter
    http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
  }

  @Bean
  RestTemplate restTemplate() throws Exception {
    SSLContext sslContext = new SSLContextBuilder()
        .loadTrustMaterial(trustStore.getURL(), trustStorePassword.toCharArray())
        .build();
    SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext);
    HttpClient httpClient = HttpClients.custom()
        .setSSLSocketFactory(socketFactory)
        .build();
    HttpComponentsClientHttpRequestFactory factory =
        new HttpComponentsClientHttpRequestFactory(httpClient);
    return new RestTemplate(factory);
  }
}
