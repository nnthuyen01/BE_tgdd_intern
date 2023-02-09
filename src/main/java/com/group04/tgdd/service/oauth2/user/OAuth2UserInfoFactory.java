package com.group04.tgdd.service.oauth2.user;

import com.group04.tgdd.exception.OAuth2AuthenticationProcessingException;
import com.group04.tgdd.utils.constant.OAuth2Constant;

import java.security.AuthProvider;
import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if(registrationId.equalsIgnoreCase(OAuth2Constant.FACEBOOK)) {
            return new FacebookOAuth2UserInfo(attributes);
        }
        else if(registrationId.equalsIgnoreCase(OAuth2Constant.GOOGLE)) {
            return new GoogleOAuth2UserInfo(attributes);
        }
        else {
            throw new OAuth2AuthenticationProcessingException("Sorry! Login with " + registrationId + " is not supported yet.");
        }
    }
}
