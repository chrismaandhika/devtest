package com.chrisma.devtest.oauth2api.resource;

import com.chrisma.devtest.oauth2api.builder.JwtTokenBuilder;
import com.chrisma.devtest.oauth2api.dto.TokenResponse;
import com.chrisma.devtest.oauth2api.service.JdbcUserDetailsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Map;

@RestController
public class AuthResource {
    private JdbcUserDetailsService userDetailsService;
    private PasswordEncoder passwordEncoder;
    private JwtTokenBuilder tokenBuilder;
    private JwtDecoder jwtDecoder;

    @Value("${jwt.access-token.hour-to-live}")
    private long accessTokenHourToLive;
    @Value("${jwt.refresh-token.hour-to-live}")
    private long refreshTokenHourToLive;

    public AuthResource(JdbcUserDetailsService userDetailsService,
                        PasswordEncoder passwordEncoder,
                        JwtTokenBuilder tokenBuilder,
                        JwtDecoder jwtDecoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.tokenBuilder = tokenBuilder;
        this.jwtDecoder = jwtDecoder;
    }

    @PostMapping(path = "/token")
    public TokenResponse requestToken(@RequestParam Map<String,String> params) {
        String grantType = params.get("grant_type").toLowerCase();
        TokenResponse token;
        switch (grantType) {
            case "password":
                token = generateToken(params.get("username"), params.get("password"));
                break;
            case "refresh_token":
                token = refreshToken(params.get("refresh_token"));
                break;
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"grant type not recognized");
        }
        return token;
    }

    private TokenResponse generateToken(String username, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if(passwordEncoder.matches(password, userDetails.getPassword())) {
            return buildTokenResponse(username, extractScope(userDetails));
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "user not authenticated");
    }

    private TokenResponse refreshToken(String refreshToken) {
        Jwt jwtToken = jwtDecoder.decode(refreshToken);
        Map<String, Object> claims = jwtToken.getClaims();
        Boolean isRefreshToken = Boolean.valueOf((String)claims.get("is_refresh_token"));
        if(!isRefreshToken) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "token is not refresh token");
        }

        if(jwtToken.getExpiresAt().isBefore(Instant.now())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "refresh token is expired");
        }

        String username = (String) claims.get("username");
        return buildTokenResponse(username,userDetailsService.getScopesByUsername(username));
    }

    private TokenResponse buildTokenResponse(String username, String scope) {
        String accessToken = buildAccessToken(username, scope, accessTokenHourToLive);
        String refreshToken = buildRefreshToken(username, scope, refreshTokenHourToLive);
        return new TokenResponse(accessToken, scope, refreshToken, accessTokenHourToLive);
    }

    private String extractScope(UserDetails details) {
        return details.getAuthorities().stream().findFirst().get().getAuthority();
    }

    private String buildAccessToken(String username, String scopes, long hourToLive) {
        return tokenBuilder.build(username, hourToLive, Map.of(
                "scope",scopes,
                "is_refresh_token", Boolean.FALSE.toString()
                ));
    }

    private String buildRefreshToken(String username, String scopes, long hourToLive) {
        return tokenBuilder.build(username, hourToLive, Map.of(
                "username", username,
                "scope",scopes,
                "is_refresh_token", Boolean.TRUE.toString()
        ));
    }
}
