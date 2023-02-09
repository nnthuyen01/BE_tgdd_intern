package com.group04.tgdd.service.oauth2;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group04.tgdd.exception.OAuth2AuthenticationProcessingException;
import com.group04.tgdd.model.Users;
import com.group04.tgdd.repository.UsersRepo;
import com.group04.tgdd.service.auth.UserDetailIplm;
import com.group04.tgdd.service.oauth2.user.OAuth2UserInfo;
import com.group04.tgdd.service.oauth2.user.OAuth2UserInfoFactory;

import com.group04.tgdd.utils.constant.RoleConstant;
import lombok.RequiredArgsConstructor;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.apache.http.client.fluent.Request;

import java.io.IOException;
import java.security.AuthProvider;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UsersRepo usersRepo;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory
                .getOAuth2UserInfo(userRequest.getClientRegistration().getRegistrationId(),oAuth2User.getAttributes());

        Users users = usersRepo.findUsersByEmail(oAuth2UserInfo.getEmail());
        if (users!=null){
            if (!users.getProvider().equals(userRequest.getClientRegistration().getRegistrationId())){
                throw new OAuth2AuthenticationProcessingException("Looks like you're signed up with " +
                        users.getProvider() + " account. Please use your " + users.getProvider() +
                        " account to login.");
            }
            users.setName(oAuth2UserInfo.getName());
            users.setAvatar(oAuth2UserInfo.getImageUrl());
            usersRepo.save(users);

        }
        else {
          users = registerNewUser(userRequest,oAuth2UserInfo);
        }

        return UserDetailIplm.create(users,oAuth2UserInfo.getAttributes());
    }
    private Users registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        Users user = new Users();

        user.setProvider(oAuth2UserRequest.getClientRegistration().getRegistrationId());

        user.setName(oAuth2UserInfo.getName());
        user.setEmail(oAuth2UserInfo.getEmail());
        user.setAvatar(oAuth2UserInfo.getImageUrl());
        user.setRole(RoleConstant.ROLE_USER);
        user.setEnable(true);
        return usersRepo.save(user);
    }


}
