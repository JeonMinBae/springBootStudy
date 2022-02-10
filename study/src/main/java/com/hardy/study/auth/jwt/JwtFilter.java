package com.hardy.study.auth.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hardy.study.auth.PrincipalDetails;
import com.hardy.study.auth.jwt.JwtTokenProvider;
import com.hardy.study.common.enums.ErrorCodeAndMessage;
import com.hardy.study.common.enums.Role;
import com.hardy.study.user.entity.UserEntity;
import com.hardy.study.user.entity.UserRepository;
import com.hardy.study.user.exception.CustomException;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class JwtFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private JwtTokenProvider jwtTokenProvider;

    public JwtFilter(UserRepository userRepository, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        System.out.println("------------------------------------------------------------------");
        System.out.println("JwtFilter");
        System.out.println(request.getRequestURI());

        String header = request.getHeader(jwtTokenProvider.HEADER_STRING);

        System.out.println("header: " + header);
        //토큰이 없거나 잘못된 토큰이면 익명(GUEST)토큰을 발행 후 전달
        String token = jwtTokenProvider.getTokenFromRequestOrNull(request);
        System.out.println("token: " + token);


        //여기서 토큰을 검증하는 과정을 거치기 떄문에 AuthticationManager가 필요 없음
        //SecurityContext에 직접 세션 생성 시 자동으로 loadByUsername이 호출
        if (!Objects.isNull(token)) {
            try {
                jwtTokenProvider.validateToken(token);
            } catch (ExpiredJwtException ex) {
                //토큰에 문제가 있거나 만료되었을 경우 재발급을 시켜야함
                System.out.println("token expired ");
                setError(request, response, HttpServletResponse.SC_UNAUTHORIZED,
                    "TOKEN ERROR", "TOKEN EXPIRED");
            } catch (Exception e) {
                System.out.println("token invalid ");
                setError(request, response, HttpServletResponse.SC_UNAUTHORIZED,
                    "TOKEN ERROR", "INVALID TOKEN");
                return;
            }
        } else {
            System.out.println("token is null ");
            UserEntity userEntity = getAnonymousUser();

            PrincipalDetails principalDetails = new PrincipalDetails(userEntity);
            Authentication authentication =
                new AnonymousAuthenticationToken("anonymousUser", principalDetails, principalDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);
            return;
        }


        String userId = jwtTokenProvider.getUserIdFromJWT(token);
//        if (!Objects.isNull(userId)) {
        System.out.println("UserId: " + userId);

        UserEntity userEntity = userRepository.findByUserId(userId)
            .orElseThrow(() -> new CustomException(ErrorCodeAndMessage.USER_NOT_FOUNT));
        System.out.println(userEntity);

        // 인증은 토큰 검증시 끝. 인증을 하기 위해서가 아닌 스프링 시큐리티가 수행해주는 권한 처리를 위해
        // 아래와 같이 토큰을 만들어서 Authentication 객체를 강제로 만들고 그걸 세션에 저장!

        PrincipalDetails principalDetails = new PrincipalDetails(userEntity);
        Authentication authentication =
            new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

        System.out.println("principalDetails.getAuthorities(): " + principalDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
//        }

        filterChain.doFilter(request, response);

    }

    private UserEntity getAnonymousUser() {
        LocalDateTime localDateTime = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(localDateTime);
        UserEntity userEntity =
            UserEntity.builder()
                .userId("GUEST_" + timestamp.getTime())
                .userEmail("guest@guest.guest")
                .userName("anonymousUser")
                .userRole(Role.ROLE_ANONYMOUS)
                .build();
        return userEntity;
    }


    private void setError(HttpServletRequest request,
                          HttpServletResponse response,
                          int SC_ERROR,
                          String errorTitle,
                          String message)
        throws IOException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(SC_ERROR);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("status", SC_ERROR);
        responseBody.put("error", errorTitle);
        responseBody.put("message", message);
        responseBody.put("path", request.getServletPath());

        ObjectMapper om = new ObjectMapper();
        om.writeValue(response.getOutputStream(), responseBody);

    }
}
