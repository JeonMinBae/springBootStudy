package com.hardy.study.auth.filter;

    import com.hardy.study.auth.PrincipalDetails;
    import com.hardy.study.auth.jwt.JwtTokenProvider;
    import com.hardy.study.common.enums.Role;
    import com.hardy.study.user.entity.UserEntity;
    import com.hardy.study.user.entity.UserRepository;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.MediaType;
    import org.springframework.security.authentication.AnonymousAuthenticationToken;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
    import org.springframework.web.filter.OncePerRequestFilter;
    import org.springframework.web.server.ResponseStatusException;

    import javax.servlet.FilterChain;
    import javax.servlet.ServletException;
    import javax.servlet.http.HttpServletRequest;
    import javax.servlet.http.HttpServletResponse;
    import java.io.IOException;
    import java.util.Objects;

//인가
public class JwtAuthorizationTestFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;


    public JwtAuthorizationTestFilter(AuthenticationManager authenticationManager,
                                      UserRepository userRepository,
                                      JwtTokenProvider jwtTokenProvider){
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
        throws IOException, ServletException {

        System.out.println("------------------------------------------------------------------");
        System.out.println("JwtAuthorizationFilter doFilterInternal");
        System.out.println(request.getRequestURI());

        String header = request.getHeader(jwtTokenProvider.HEADER_STRING);

        System.out.println("header: " + header);
        //토큰이 없거나 잘못된 토큰이면 익명(GUEST)토큰을 발행 후 전달
        if(Objects.isNull(header) || !header.startsWith(jwtTokenProvider.TOKEN_PREFIX)){
            UserEntity userEntity =
                UserEntity.builder()
                    .userId("GUEST_")
                    .userEmail("guest@guest.guest")
                    .userName("anonymousUser")
                    .userRole(Role.ROLE_GUEST)
                    .build();

            PrincipalDetails principalDetails = new PrincipalDetails(userEntity);

//            Authentication authentication =
//                new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
            AnonymousAuthenticationToken authentication =
                new AnonymousAuthenticationToken("anonymousUser", principalDetails, principalDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);


            String jwtToken = jwtTokenProvider.generateToken(authentication);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_OK);
            response.addHeader(jwtTokenProvider.HEADER_STRING, jwtTokenProvider.TOKEN_PREFIX+jwtToken);

            chain.doFilter(request, response);
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return ;
        }




        String token = header.replace(jwtTokenProvider.TOKEN_PREFIX, "");
        System.out.println("token: " + token);
        //여기서 토큰을 검증하는 과정을 거치기 떄문에 AuthticationManager가 필요 없음
        //SecurityContext에 직접 세션 생성 시 자동으로 loadByUsername이 호출

        try{
            jwtTokenProvider.validateToken(token);
        } catch(Exception e){
            System.out.println("token invalid ");
            chain.doFilter(request, response);
            return ;
        }


        String userId = jwtTokenProvider.getUserIdFromJWT(token);
        if(!Objects.isNull(userId)){
            System.out.println("UserId: "+ userId);
            UserEntity userEntity;

            if(userId.startsWith("GUEST_")){
                userEntity =
                    UserEntity.builder()
                        .userId("GUEST_")
                        .userEmail("guest@guest.guest")
                        .userName("anonymousUser")
                        .userRole(Role.ROLE_GUEST)
                        .build();
            }else{
                userEntity = userRepository.findByUserId(userId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NO_CONTENT, "User Not Found [" + userId + "]"));
                System.out.println(userEntity);
                // 인증은 토큰 검증시 끝. 인증을 하기 위해서가 아닌 스프링 시큐리티가 수행해주는 권한 처리를 위해
                // 아래와 같이 토큰을 만들어서 Authentication 객체를 강제로 만들고 그걸 세션에 저장!
            }
            PrincipalDetails principalDetails = new PrincipalDetails(userEntity);
            Authentication authentication =
                new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

            System.out.println("principalDetails.getAuthorities(): " + principalDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwtToken = jwtTokenProvider.generateToken(authentication);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_OK);
            response.addHeader(jwtTokenProvider.HEADER_STRING, jwtTokenProvider.TOKEN_PREFIX+jwtToken);

        }




        chain.doFilter(request, response);
    }
}

