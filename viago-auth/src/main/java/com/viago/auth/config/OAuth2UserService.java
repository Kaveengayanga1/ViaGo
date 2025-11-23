package com.viago.auth.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
@Slf4j
public class OAuth2UserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        
        String provider = userRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes = oAuth2User.getAttributes();
        
        log.info("OAuth2 login attempt from provider: {}", provider);
        log.debug("OAuth2 attributes: {}", attributes);
        
        // Create authorities - default to RIDER role for OAuth2 users
        var authorities = Collections.singletonList(
            new SimpleGrantedAuthority("ROLE_RIDER")
        );
        
        // Return OAuth2User with 'email' as the name attribute key
        // This matches user-name-attribute: email in application.yml
        // Note: oAuth2User.getName() returns the VALUE (email address), not the KEY
        return new DefaultOAuth2User(
            authorities,
            attributes,
            "email" // name attribute key - must match user-name-attribute in application.yml
        );
    }
}

