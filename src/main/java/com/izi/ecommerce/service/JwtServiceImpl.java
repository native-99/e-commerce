package com.izi.ecommerce.service;

import com.izi.ecommerce.config.JwtSecretConfig;
import com.izi.ecommerce.model.UserInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtServiceImpl implements JwtService {

    private final JwtSecretConfig jwtSecretConfig;
    private final SecretKey signKey;

    @Override
    public String generateToken(UserInfo userInfo) {
        Instant now = Instant.now();
        Instant expiredAt = now.plus(jwtSecretConfig.getJwtExpirationTime());

        return Jwts.builder()
                .setSubject(userInfo.getUsername())
                .claim("roles", userInfo.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiredAt))
                .signWith(signKey, SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException exception) {
            log.warn("Invalid JWT token: {}", exception.getMessage());
            return false;
        }
    }

    @Override
    public String getUsernameFromToken(String token) {
        return getClaims(token).getSubject();
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
