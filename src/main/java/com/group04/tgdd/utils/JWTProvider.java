package com.group04.tgdd.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.group04.tgdd.model.Users;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public class JWTProvider {

    public static String createJWT(Users user, HttpServletRequest request){
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        return JWT.create()
                .withSubject(user.getId().toString())
                .withExpiresAt(new Date(System.currentTimeMillis()+10*60*1000*6*24*15))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("role",user.getRole())
                .sign(algorithm);
    }
}
