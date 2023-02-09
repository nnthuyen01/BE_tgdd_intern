package com.group04.tgdd.config;

import com.group04.tgdd.filter.UserAuthorizationFilter;
import com.group04.tgdd.service.auth.UserDetailServiceIplm;
import com.group04.tgdd.service.oauth2.CustomOAuth2UserService;
import com.group04.tgdd.service.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.group04.tgdd.service.oauth2.OAuth2AuthenticationFailureHandler;
import com.group04.tgdd.service.oauth2.OAuth2LoginSuccessHandler;


import com.group04.tgdd.utils.constant.RoleConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailServiceIplm userDetailsService;
    @Autowired
    private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;
    @Autowired
    private OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

    private static final String[] WHITE_LIST_URLS = {
            "/api/registerTest",
            "/api/login/**",
            "/api/phoneLogin/**",
            "/api/register/phone",
            "/api/register-email",
            "/api/resetPassword/**",
            "/api/verifyRegistration/*",
            "/api/resendVerifyToken",
            "/api/savePassword",
            "/api/refreshToken",
            "/api/voucher",
            // -- Swagger UI v2
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            // -- Swagger UI v3 (OpenAPI)
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/ws/**"
    };

    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieOAuth2AuthorizationRequestRepository(){
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues());
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers(WHITE_LIST_URLS).permitAll();

        http
                .authorizeRequests()
                .antMatchers("/api/admin/**").hasAnyAuthority(RoleConstant.ROLE_ADMIN)
                .antMatchers(HttpMethod.POST,"/api/**").hasAnyAuthority(RoleConstant.ROLE_USER, RoleConstant.ROLE_ADMIN)
                .antMatchers(HttpMethod.PUT,"/api/**").hasAnyAuthority(RoleConstant.ROLE_USER, RoleConstant.ROLE_ADMIN)
                .antMatchers(HttpMethod.DELETE,"/api/**").hasAnyAuthority(RoleConstant.ROLE_USER, RoleConstant.ROLE_ADMIN)
                .antMatchers(HttpMethod.GET, "/api/**").permitAll()

                .and().oauth2Login()
                .authorizationEndpoint()
                .baseUri("/oauth2/authorize")
                .authorizationRequestRepository(cookieOAuth2AuthorizationRequestRepository())
                .and()
                .redirectionEndpoint()
                .baseUri("/oauth2/callback/*")
                .and()
                .userInfoEndpoint()
                .userService(customOAuth2UserService)
                .and()
                .successHandler(oAuth2LoginSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler);

        http.addFilterBefore(new UserAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
