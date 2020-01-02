package me.ebenezergraham.honours.platform.security.oauth2;

import me.ebenezergraham.honours.platform.exception.OAuth2AuthenticationProcessingException;
import me.ebenezergraham.honours.platform.model.AuthProvider;
import me.ebenezergraham.honours.platform.model.RoleName;
import me.ebenezergraham.honours.platform.model.User;
import me.ebenezergraham.honours.platform.repository.UserRepository;
import me.ebenezergraham.honours.platform.security.UserPrincipal;
import me.ebenezergraham.honours.platform.security.oauth2.user.OAuth2UserInfo;
import me.ebenezergraham.honours.platform.security.oauth2.user.OAuth2UserInfoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

  @Autowired
  private UserRepository userRepository;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
    OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

    try {
      return processOAuthRequest(oAuth2UserRequest, oAuth2User);
    } catch (AuthenticationException ex) {
      throw ex;
    } catch (Exception ex) {
      // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
      throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
    }
  }

  private OAuth2User processOAuthRequest(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
    oAuth2UserRequest.getAccessToken().getTokenValue();
    OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());
    if (StringUtils.isEmpty(oAuth2UserInfo.getUserName())) {
      throw new OAuth2AuthenticationProcessingException("Weird!, There is no username on your account");
    }

    if (StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
      //Should emails be optional
      throw new OAuth2AuthenticationProcessingException("Set a public email address for your account");
    }

    Optional<User> storedUser = userRepository.findByUsername(oAuth2UserInfo.getUserName());
    User user;
    if (storedUser.isPresent()) {
      user = storedUser.get();
      if (!user.getProvider().equals(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))) {
        throw new OAuth2AuthenticationProcessingException("You have signed up with " +
            user.getProvider() + " account. Use your " + user.getProvider() +
            " account to login.");
      }
      user = updateExistingUser(user, oAuth2UserInfo);
    } else {
      user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
    }

    return UserPrincipal.create(user, oAuth2User.getAttributes());
  }

  private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
    User user = new User();

    user.setProvider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
    user.setProviderId(oAuth2UserInfo.getId());
    user.setName(oAuth2UserInfo.getName());
    user.setUsername(oAuth2UserInfo.getUserName());
    user.setEmail(oAuth2UserInfo.getEmail());
    user.setImageUrl(oAuth2UserInfo.getImageUrl());
    if (user.getUsername().equals("ebenezergraham")) {
      user.setPoints(10050);
    }
    if(Boolean.valueOf(String.valueOf(oAuth2UserInfo.getAttributes().get("site_admin")))){
      user.setRoles(RoleName.ROLE_ADMIN);
    }else {
      user.setRoles(RoleName.ROLE_USER);
    }
    return userRepository.save(user);
  }

  private User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
    existingUser.setName(oAuth2UserInfo.getName());
    existingUser.setUsername(oAuth2UserInfo.getUserName());
    existingUser.setEmail(oAuth2UserInfo.getEmail());
    existingUser.setImageUrl(oAuth2UserInfo.getImageUrl());
    if(Boolean.valueOf(String.valueOf(oAuth2UserInfo.getAttributes().get("site_admin")))){
      existingUser.setRoles(RoleName.ROLE_ADMIN);
    }else {
      existingUser.setRoles(RoleName.ROLE_USER);
    }
    return userRepository.save(existingUser);
  }

}
