package com.group04.tgdd.service.oauth2;

import com.group04.tgdd.model.Users;
import com.group04.tgdd.repository.UsersRepo;
import com.group04.tgdd.service.auth.UserDetailIplm;

import com.group04.tgdd.utils.JWTProvider;
import com.group04.tgdd.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${redirect.url.oauth2}")
    private String redirectUrl;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserDetailIplm users = (UserDetailIplm) authentication.getPrincipal();
        String targetUrl = createUrlTarget(users.getUsers(),request);


        getRedirectStrategy().sendRedirect(request,response,targetUrl);
    }

    public String createUrlTarget(Users user,HttpServletRequest request){
        String token = JWTProvider.createJWT(user,request);
        String urlTarger = UriComponentsBuilder.fromUriString(redirectUrl)
                .queryParam("token",token)
                .build().toUriString();
        return urlTarger;
    }
}
