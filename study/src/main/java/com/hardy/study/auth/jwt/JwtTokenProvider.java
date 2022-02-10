package com.hardy.study.auth.jwt;

import com.hardy.study.auth.PrincipalDetails;
import com.hardy.study.common.enums.ErrorCodeAndMessage;
import com.hardy.study.common.enums.Role;
import com.hardy.study.user.exception.CustomException;
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
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Objects;

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
    public JwtToken generateToken(Authentication authentication, Role role){
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + this.JWT_TOKEN_EXPIRED_MS);
        String userId = ((PrincipalDetails)authentication.getPrincipal()).getUser().getUserId();

        String token = Jwts.builder()
            .setSubject(userId)
            .claim("Role", role)
            .setIssuedAt(new Date()) // 현재 시간 기반으로 생성
            .setExpiration(expiryDate) // 만료 시간 세팅
            .signWith(secretKey, SignatureAlgorithm.HS512) // 사용할 암호화 알고리즘, signature에 들어갈 secret 값 세팅
            .compact();

        JwtToken.JwtTokenBuilder builder = JwtToken.builder()
            .token(token)
            .userId(userId)
            .ExpireDate(expiryDate)
            .role(role)
            ;

        return builder.build();
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
    public boolean validateToken(String token) throws ExpiredJwtException{
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            throw new CustomException(ErrorCodeAndMessage.TOKEN_SIGNATURE_INVALID);
        } catch (MalformedJwtException ex) {
            throw new CustomException(ErrorCodeAndMessage.TOKEN_INVALID);
        } catch (UnsupportedJwtException ex) {
            throw new CustomException(ErrorCodeAndMessage.UNSUPPORTED_TOKEN);
        } catch (IllegalArgumentException ex) {
            throw new CustomException(ErrorCodeAndMessage.TOKEN_CLAIMS_EMPTY);
        }
//        return false;
    }

    public String getTokenFromRequestOrNull(HttpServletRequest request){
        String token = null;
        String header = request.getHeader(HEADER_STRING);

        if(Objects.isNull(header) || !header.startsWith(TOKEN_PREFIX)){
            return token;
        }
        token = header.replace(TOKEN_PREFIX, "");

        return token;
    }

}
