package com.hardy.study.auth.filter;

import com.hardy.study.auth.PrincipalDetails;
import com.hardy.study.auth.jwt.JwtTokenProvider;
import com.hardy.study.common.enums.Role;
import com.hardy.study.user.entity.UserEntity;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAnonymousAuthenticationFilter extends AnonymousAuthenticationFilter {

    private final String key;
    private final JwtTokenProvider jwtTokenProvider;

    public CustomAnonymousAuthenticationFilter(String key, JwtTokenProvider jwtTokenProvider) {
        super(key);
        this.key = key;
        this.jwtTokenProvider= jwtTokenProvider;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        System.out.println("------------------------------------------------------------------");
        System.out.println("CustomAnonymousAuthenticationFilter doFilter");


        Authentication authentication = createAuthentication((HttpServletRequest) req);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwtToken = jwtTokenProvider.generateToken(authentication);
        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ((HttpServletResponse)res).setStatus(HttpServletResponse.SC_OK);
        ((HttpServletResponse)res).addHeader(jwtTokenProvider.HEADER_STRING, jwtTokenProvider.TOKEN_PREFIX+jwtToken);

        chain.doFilter(req, res);
//        super.doFilter(req, res, chain);
    }

    @Override
    protected Authentication createAuthentication(HttpServletRequest request) {
        System.out.println("CustomAnonymousAuthenticationFilter createAuthentication");
        UserEntity userEntity =
            UserEntity.builder()
                .userId("GUEST_")
                .userEmail("guest@guest.guest")
                .userName("anonymousUser")
                .userRole(Role.ROLE_GUEST)
                .build();
        PrincipalDetails principalDetails = new PrincipalDetails(userEntity);
        AnonymousAuthenticationToken token =
            new AnonymousAuthenticationToken(this.key, principalDetails, principalDetails.getAuthorities());

        return token;
    }
}
