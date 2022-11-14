package com.example.sprintjabackend.utility;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.sprintjabackend.model.UserPrincipal;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;


import static com.example.sprintjabackend.constant.SecurityConstant.*;
import static java.util.Arrays.stream;


@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secret;

    //method to create user token below
    public String generateJwtToken(UserPrincipal userPrincipal) {
        String[] claims = getClaimsFromUser(userPrincipal);
        return JWT.create().withIssuer(SPRINT_JA)
                .withAudience(SPRINT_JA_ADMINISTRATION)
                .withIssuedAt(new Date())
                .withSubject(userPrincipal.getUsername())
                .withArrayClaim(AUTHORITIES, claims)
                .withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(secret.getBytes()));
    }

    //method gets the Authorities from the token assigned to the user
    public List<GrantedAuthority> grantedAuthorities(String token) {
        String[] claims = getClaimsFromToken(token);
        return stream(claims).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    public Authentication getAuthentication(String username, List<GrantedAuthority> authorities, HttpServletRequest httpServletRequest) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, "", authorities);
        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
        return usernamePasswordAuthenticationToken;
    }

    public boolean isTokenValid(String username, String token) {
        JWTVerifier jwtVerifier = getJWTVerifier();
        return StringUtils.isNoneEmpty(username) && !isTokenExpired(jwtVerifier, token);
    }

    public boolean isTokenExpired(JWTVerifier jwtVerifier, String token) {
        Date expiration = jwtVerifier.verify(token).getExpiresAt();
        return expiration.before(new Date());
    }

    public String getSubject(String token) {
        JWTVerifier jwtVerifier = getJWTVerifier();
        return jwtVerifier.verify(token).getIssuer();
    }

    //method gets all the claims from the token assigned to a user
    private String[] getClaimsFromToken(String token) {
        JWTVerifier verifier = getJWTVerifier();
        return verifier.verify(token).getClaim(AUTHORITIES).asArray(String.class);
    }

    //method verifies a token
    private JWTVerifier getJWTVerifier() throws JWTVerificationException {
        JWTVerifier verifier;
        Algorithm algorithm = Algorithm.HMAC512(secret);
        verifier = JWT.require(algorithm)
                .withIssuer(SPRINT_JA)
                .build();
        return verifier;
    }

    // method below get users authorities from user Principal
    private String[] getClaimsFromUser(UserPrincipal userPrincipal) {
        List<String> authorities = new ArrayList<>();
        for (GrantedAuthority grantedAuthority : userPrincipal.getAuthorities()) {
            authorities.add(grantedAuthority.getAuthority());
        }
        return authorities.toArray(new String[0]);
    }

}


