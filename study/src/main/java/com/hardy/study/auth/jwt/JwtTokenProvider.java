package com.hardy.study.auth.jwt;

import com.hardy.study.auth.PrincipalDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private static final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
//    private static final String base64SecretKey = Encoders.BASE64.encode(secretKey.getEncoded());
    //토큰 유효시간
    private final long JWT_TOKEN_EXPIRED_MS = 60000L; // 10 * 60 * 1000
    private final long JWT_REFRESH_TOKEN_EXPIRED_MS = 180000L; // 30 * 60 * 1000

    public final String TOKEN_PREFIX = "Bearer ";
    public final String HEADER_STRING = "Authorization";


    //토큰 생성
    public String generateToken(Authentication authentication){
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + this.JWT_TOKEN_EXPIRED_MS);

        return Jwts.builder()
            .setSubject(((PrincipalDetails)authentication.getPrincipal()).getUser().getUserId())
            .claim("test", "testValue")
            .setIssuedAt(new Date()) // 현재 시간 기반으로 생성
            .setExpiration(expiryDate) // 만료 시간 세팅
            .signWith(secretKey, SignatureAlgorithm.HS512) // 사용할 암호화 알고리즘, signature에 들어갈 secret 값 세팅
            .compact();
    }

    // Jwt 토큰에서 아이디 추출
    public String getUserIdFromJWT(String token) {
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .getBody();

        return claims.getSubject();
    }

    // Jwt 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "JWT claims string is empty");
        }
//        return false;
    }

}
