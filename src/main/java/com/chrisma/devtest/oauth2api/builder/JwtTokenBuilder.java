package com.chrisma.devtest.oauth2api.builder;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Component;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

@Component
public class JwtTokenBuilder {

    private RSAPublicKey publicKey;
    private RSAPrivateKey privateKey;

    public JwtTokenBuilder(RSAPublicKey publicKey, RSAPrivateKey privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public String build(String subject, long hourToLive, Map<String, String> claims) {
        JWTCreator.Builder builder = JWT.create().withSubject(subject);
        claims.forEach(builder::withClaim);
        Date currentDate = new Date();
        return builder.withNotBefore(currentDate)
                .withExpiresAt(getExpiredDate(hourToLive, currentDate))
                .sign(Algorithm.RSA256(publicKey, privateKey));
    }

    private Date getExpiredDate(long hourToLive, Date current) {
        LocalDateTime time = current.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        time = time.plusHours(hourToLive);
        return Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
    }
}
