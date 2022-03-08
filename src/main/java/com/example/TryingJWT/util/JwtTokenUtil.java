package com.example.TryingJWT.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.TryingJWT.domain.Account;
import com.example.TryingJWT.domain.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class JwtTokenUtil {

    private static final Integer ACCESS_TOKEN_VALID_INTERVAL = 1000;
    private static final Integer REFRESH_TOKEN_VALID_INTERVAL = 30 * 60 * 1000;
    private static final Algorithm algorithm = Algorithm.HMAC256("secretKey".getBytes(StandardCharsets.UTF_8));

    public static Map<String, String> getTokens(Authentication authentication, HttpServletRequest request) {
        User user = (User) authentication.getPrincipal();
        String accessToken = getToken(ACCESS_TOKEN_VALID_INTERVAL, request, user);
        String refreshToken = getToken(REFRESH_TOKEN_VALID_INTERVAL, request, user);
        return buildTokensMap(accessToken, refreshToken);
    }

    public  static Map<String, String> buildTokensMap(String accessToken, String refreshToken) {
        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", accessToken);
        tokens.put("refresh_token", refreshToken);
        return tokens;
    }

    private static String getToken(Integer validInterval, HttpServletRequest request, User user) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + validInterval))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
    }

    public static DecodedJWT verifyTokenAndGetDecodedJWT(String token) {
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        return decodedJWT;
    }

    public static String getUsername(DecodedJWT decodedJWT) {
        return decodedJWT.getSubject();
    }

    public static String[] getRoles(DecodedJWT decodedJWT) {
        return decodedJWT.getClaim("roles").asArray(String.class);
    }

    public static String refreshAccessToken(Account account, HttpServletRequest request){
        return JWT.create()
                .withSubject(account.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALID_INTERVAL))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", account.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                .sign(algorithm);
    }
}
