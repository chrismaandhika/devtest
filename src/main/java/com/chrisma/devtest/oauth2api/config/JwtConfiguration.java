package com.chrisma.devtest.oauth2api.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
@Slf4j
public class JwtConfiguration {
    @Value("${keystore.path}")
    private String keyStorePath;

    @Value("${keystore.password}")
    private String keyStorePassword;

    @Value("${keystore.key.alias}")
    private String keyAlias;

    @Bean
    public KeyStore keyStore() {
        try {
            KeyStore keyStore;
            keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            InputStream ksStream = new ClassPathResource(keyStorePath).getInputStream();
            keyStore.load(ksStream, keyStorePassword.toCharArray());
            return keyStore;
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException e) {
            log.error("error in creating keystore", e);
        }
        throw new IllegalArgumentException("invalid keystore");
    }

    @Bean
    public RSAPrivateKey privateKey(KeyStore keyStore) {
        try {
            return (RSAPrivateKey) keyStore.getKey(keyAlias, keyStorePassword.toCharArray());
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            log.error("error in creating private key", e);
        }
        throw new IllegalArgumentException("invalid private key");
    }

    @Bean
    public RSAPublicKey publicKey(KeyStore keyStore) {
        try {
            return (RSAPublicKey) keyStore.getCertificate(keyAlias).getPublicKey();
        } catch (KeyStoreException e) {
            log.error("error in creating public key", e);
        }
        throw new IllegalArgumentException("invalid public key");
    }

    @Bean
    public JwtDecoder jwtDecoder(RSAPublicKey publicKey) {
        return NimbusJwtDecoder.withPublicKey(publicKey).build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
