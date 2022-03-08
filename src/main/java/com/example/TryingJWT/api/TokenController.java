package com.example.TryingJWT.api;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.TryingJWT.domain.Account;
import com.example.TryingJWT.service.impl.TokenServiceImpl;
import com.example.TryingJWT.util.CommonMethodsUtil;
import com.example.TryingJWT.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@RequestMapping("/api")
public class TokenController {

    @Autowired
    TokenServiceImpl tokenService;

    @GetMapping("/token/refresh")
    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        tokenService.refreshToken(request, response);
    }
}
