package com.example.TryingJWT.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.TryingJWT.util.CommonMethodsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import com.example.TryingJWT.util.JwtTokenUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String servletPath = request.getServletPath();
        if(servletPath.equals("/api/login") || servletPath.equals("/api/token/refresh")){
            filterChain.doFilter(request, response);
        }else{
            String authenticationHeader = request.getHeader(AUTHORIZATION);
            if(authenticationHeader != null && authenticationHeader.startsWith("Bearer ")){
                try{
                    String token = authenticationHeader.substring("Bearer ".length());
                    DecodedJWT decodedJWT = JwtTokenUtil.verifyTokenAndGetDecodedJWT(token);

                    String username = JwtTokenUtil.getUsername(decodedJWT);
                    String[] roles = JwtTokenUtil.getRoles(decodedJWT);
                    Collection<SimpleGrantedAuthority> authorities = buildAuthorities(roles);
                    setAuthenticationToken(username, authorities);

                    filterChain.doFilter(request, response);

                }catch(Exception exc){
                    log.error("Error occurred while extracting token {}", exc.getMessage());
                    CommonMethodsUtil.errorResponse(response, exc);
                }
            }else{
                filterChain.doFilter(request, response);
            }
        }
    }

    private Collection<SimpleGrantedAuthority> buildAuthorities(String[] roles) {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        stream(roles).forEach(role ->{
            authorities.add(new SimpleGrantedAuthority(role));
        });
        return authorities;
    }

    private void setAuthenticationToken(String username, Collection<SimpleGrantedAuthority> authorities) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken(username, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
    }

}
