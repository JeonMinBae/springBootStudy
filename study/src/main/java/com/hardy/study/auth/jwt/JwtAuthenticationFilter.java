package com.hardy.study.auth.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hardy.study.auth.PrincipalDetails;
import com.hardy.study.auth.dto.SignInDto;
import com.hardy.study.auth.jwt.JwtTokenProvider;
import com.hardy.study.auth.oauth2.OAuth2TokenRequestDto;
import com.hardy.study.auth.oauth2.OAuth2VerifyResponseDto;
import com.hardy.study.auth.service.AuthService;
import com.hardy.study.common.enums.ErrorCodeAndMessage;
import com.hardy.study.common.enums.Role;
import com.hardy.study.user.entity.UserEntity;
import com.hardy.study.user.exception.CustomException;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

//인증
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
        throws AuthenticationException {
//        return super.attemptAuthentication(request, fdtresponse);
        System.out.println("------------------------------------------------------------------");
        System.out.println("JwtAuthenticationFilter attemptAuthentication");

        System.out.println("request.getMethod(): " + request.getMethod());


        ObjectMapper om = new ObjectMapper();

        SignInDto signInDto = null;
        try {
            signInDto = om.readValue(request.getInputStream(), SignInDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(signInDto.getUserId(), signInDto.getUserPassword());
        System.out.println("UsernamePasswordAuthenticationToken 생성");
        System.out.println(authenticationToken);
        // authenticate()시 AuthenticationProvider가 UserDetailsService의
        // loadUserByUsername(토큰의 첫번째 파라메터) 를 호출
        // UserDetails를 리턴받아서 토큰의 두번째 파라메터(credential)과
        // UserDetails(DB값)의 getPassword()함수로 비교해서 동일하면
        // Authentication 객체를 만들어서 필터체인으로 리턴

        // Tip: AuthenticationProvider의 디폴트 서비스는 UserDetailsService 타입
        // 결론은 인증 프로바이더에게 알려줄 필요가 없음.

        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("Authentication: " + principalDetails.getUsername());


        return authentication;

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult)
        throws IOException, ServletException {

        System.out.println("------------------------------------------------------------------");
        System.out.println("successfulAuthentication 진입");
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        System.out.println("authResult: " + authResult);
//        String jwtToken = jwtTokenProvider.generateToken(authResult);
//
//
//        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//        response.setStatus(HttpServletResponse.SC_OK);
//        response.addHeader(jwtTokenProvider.HEADER_STRING, jwtTokenProvider.TOKEN_PREFIX + jwtToken);
//
//        Map<String, Object> responseBody = new HashMap<>();
//        responseBody.put("userId", principalDetails.getUser().getUserId());
//        responseBody.put("status", 200);
//        responseBody.put("access_token", jwtToken);
//
//        ObjectMapper om = new ObjectMapper();
//        om.writeValue(response.getOutputStream(), responseBody);


    }

    public static String getBody(HttpServletRequest request) throws IOException {

        String body = null;
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    throw ex;
                }
            }
        }

        body = stringBuilder.toString();
        return body;
    }


}
