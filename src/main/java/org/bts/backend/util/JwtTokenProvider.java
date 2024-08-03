package org.bts.backend.util;

import org.springframework.beans.factory.annotation.Value;
import io.jsonwebtoken.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access_expiration}")
    private long accessTokenExpiration;

    private final String JwtPrefix = "Bearer ";
    public String fromHeader(String header) {
        return header.replace(JwtPrefix, "");
    }
    public String createAccessToken(Authentication authentication) {
        Claims claims = Jwts.claims().setSubject(authentication.getName());
        claims.put("role", authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));

        Date now = new Date();
        return JwtPrefix + Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessTokenExpiration))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String createRefreshToken() {
        Claims claims = Jwts.claims();
        claims.put("value", UUID.randomUUID().toString());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // 토큰의 서브젝트인 이메일 추출
    public String getEmailForAccessToken(String a_token) {
        String token = fromHeader(a_token);
        return getClaimes(token).getSubject();
    }

    // 토큰의 클레임 추출
    public Claims getClaimes(String token) {
        if (token.startsWith(JwtPrefix)){
            token = fromHeader(token);
        }
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        }
        catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public String getRefreshTokenId(String r_token) {
        return getClaimes(r_token).get("value").toString();
    }

    public Date getExpirationTime(String token) {
        if(token.startsWith(JwtPrefix)){
            token = fromHeader(token);
        }
        return getClaimes(token).getExpiration();
    }

    public List<String> getRole(String a_token) {
        return getClaimes(a_token).get("role", List.class);
    }

    // Token의 UUID 와 RefreshTokenId 비교
    public boolean sameRefreshToken(String r_token, String tokenId) {
        return getRefreshTokenId(r_token).equals(tokenId);
    }

    public boolean validateToken(String token) {
        if (token.startsWith(JwtPrefix)){
            token = fromHeader(token);
        }
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        }
        catch (SignatureException e) {
            throw new IllegalArgumentException("Invalid JWT signature");
        }
        catch (MalformedJwtException e) {
            throw new IllegalArgumentException("Invalid JWT token");
        }
        catch (ExpiredJwtException e) {
            throw new IllegalArgumentException("Expired JWT token");
        }
        catch (UnsupportedJwtException e) {
            throw new IllegalArgumentException("Unsupported JWT token");
        }
        catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("JWT claims string is empty.");
        }
    }
}
