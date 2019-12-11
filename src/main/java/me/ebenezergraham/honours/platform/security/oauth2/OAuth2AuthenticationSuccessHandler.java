package me.ebenezergraham.honours.platform.security.oauth2;


import me.ebenezergraham.honours.platform.configuration.AppProperties;
import me.ebenezergraham.honours.platform.exception.BadRequestException;
import me.ebenezergraham.honours.platform.model.AuthProvider;
import me.ebenezergraham.honours.platform.model.User;
import me.ebenezergraham.honours.platform.repository.UserRepository;
import me.ebenezergraham.honours.platform.security.TokenProvider;
import me.ebenezergraham.honours.platform.security.UserPrincipal;
import me.ebenezergraham.honours.platform.util.CookieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;

import static me.ebenezergraham.honours.platform.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;


@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private TokenProvider tokenProvider;
    private AppProperties appProperties;
    private HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    @Value("${spring.security.oauth2.client.registration.github.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.github.client-secret}")
    private String clientSecret;

    OAuth2AuthorizedClientRepository oAuth2ClientRepository;

    UserRepository userRepository;

    @Autowired
    OAuth2AuthenticationSuccessHandler(UserRepository userRepository, OAuth2AuthorizedClientRepository oAuth2ClientRepository,
                                       TokenProvider tokenProvider, AppProperties appProperties,
                                       HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository) {
        this.tokenProvider = tokenProvider;
        this.userRepository = userRepository;
        this.oAuth2ClientRepository = oAuth2ClientRepository;
        this.appProperties = appProperties;
        this.httpCookieOAuth2AuthorizationRequestRepository = httpCookieOAuth2AuthorizationRequestRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        Optional<User> user = userRepository.findByEmail(((UserPrincipal)authentication.getPrincipal()).getEmail());

        String targetUrl = null;
        if(user.isPresent()){
            OAuth2AuthorizedClient client = oAuth2ClientRepository.loadAuthorizedClient(AuthProvider.github.name(),
                authentication,request);
            String accessToken = client.getAccessToken().getTokenValue();
            targetUrl = buildRedirectUrl(request, response, authentication,accessToken);
        }

        if (response.isCommitted()) {
            logger.debug("Unable to redirect to " + targetUrl);
            return;
        }

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    private String buildRedirectUrl(HttpServletRequest request,
                                      HttpServletResponse response,
                                      Authentication authentication,
                                      String accessToken) {
        Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        /*if(redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw new BadRequestException("Sorry! Unauthorized Redirect URI");
        }*/
        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

        Object [] roles = ((UserPrincipal)authentication.getPrincipal()).getAuthorities().toArray();
        String token = tokenProvider.createJwtToken(authentication, roles);
        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("token", token)
                .queryParam("access_token", accessToken)
                .queryParam("roles", roles)
                .queryParam("username", ((UserPrincipal) authentication.getPrincipal()).getUsername())
                .build().toUriString();
    }

    private void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);

        return appProperties.getOauth2().getAuthorizedRedirectUris()
                .stream()
                .anyMatch(authorizedRedirectUri -> {
                    // Only validate host and port. Let the clients use different paths if they want to
                    URI authorizedURI = URI.create(authorizedRedirectUri);
                    if(authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                            && authorizedURI.getPort() == clientRedirectUri.getPort()) {
                        return true;
                    }
                    return false;
                });
    }
}
