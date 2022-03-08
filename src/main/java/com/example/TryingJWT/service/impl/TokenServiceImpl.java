package com.example.TryingJWT.service.impl;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.TryingJWT.domain.Account;
import com.example.TryingJWT.service.AccountService;
import com.example.TryingJWT.service.TokenService;
import com.example.TryingJWT.util.CommonMethodsUtil;
import com.example.TryingJWT.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    AccountService accountService;

    public TokenServiceImpl(AccountService accountService){
        this.accountService = accountService;
    }

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authenticationHeader = request.getHeader(AUTHORIZATION);
        if(authenticationHeader != null && authenticationHeader.startsWith("Bearer ")){
            try{
                String refreshToken = authenticationHeader.substring("Bearer ".length());
                DecodedJWT decodedJWT = JwtTokenUtil.verifyTokenAndGetDecodedJWT(refreshToken);
                String username = JwtTokenUtil.getUsername(decodedJWT);
                Account account = accountService.getAccount(username);
                String newAccessToken = JwtTokenUtil.refreshAccessToken(account, request);
                Map<String, String> tokens = JwtTokenUtil.buildTokensMap(newAccessToken, refreshToken);
                CommonMethodsUtil.tokensJSONResponse(response, tokens);

            }catch(Exception exc){
                CommonMethodsUtil.errorResponse(response, exc);
            }
        }else{
            throw new RuntimeException("Refresh Token is missing") ;
        }
    }
}
