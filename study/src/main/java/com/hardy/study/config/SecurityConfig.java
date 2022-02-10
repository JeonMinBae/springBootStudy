package com.hardy.study.config;

import com.hardy.study.auth.jwt.CustomAccessDeniedHandler;
import com.hardy.study.auth.jwt.CustomAnonymousAuthenticationFilter;
import com.hardy.study.auth.jwt.JwtAuthenticationFilter;
import com.hardy.study.auth.jwt.JwtFilter;
import com.hardy.study.auth.jwt.JwtAuthenticationEntryPoint;
import com.hardy.study.auth.jwt.JwtTokenProvider;
import com.hardy.study.auth.service.AuthService;
import com.hardy.study.user.entity.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;

@Configuration
@EnableWebSecurity //스프링 시큐리티 필터가 스프링 필터체인에 등록, debug = true 디버그로그 출력
@EnableGlobalMethodSecurity(prePostEnabled = true) //prePostEnabled @PreAuthorize @PostAuthorize 사용
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    //해당 메서드의 리턴되는 오브젝트를 IoC로 등록해줌

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;



//    @Bean
//    public JwtAuthenticationFilter getJwtAuthenticationFilter() throws Exception{
//        final JwtAuthenticationFilter filter = new JwtAuthenticationFilter(authenticationManager(), jwtTokenProvider);
//        filter.setFilterProcessesUrl("/api/auth/sign-in");
//        return filter;
//    }

    @Bean
    public PasswordEncoder encodePassword(){
        PasswordEncoder passwordEncoder =
            PasswordEncoderFactories.createDelegatingPasswordEncoder();

//        or
//        String idForEncode = "bcrypt";
//        Map encoders = new HashMap<>();
//
//        encoders.put("noop", new BCryptPasswordEncoder());
//        encoders.put("bcrypt", new BCryptPasswordEncoder());
//        encoders.put("scrypt", new BCryptPasswordEncoder());
//        encoders.put("pbkdf2", new BCryptPasswordEncoder());
//        encoders.put("sha256", new BCryptPasswordEncoder());
//
//        PasswordEncoder passwordEncoder =
//            new DelegatingPasswordEncoder(idForEncode, encoders);
        return passwordEncoder;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception{

//        final JwtAuthenticationFilter jwtAuthenticationFilter
//            = new JwtAuthenticationFilter(authenticationManager(), jwtTokenProvider);
//        jwtAuthenticationFilter.setPostOnly(true);
//        jwtAuthenticationFilter.setFilterProcessesUrl("/api/auth/sign-in");

        http
            //cors 사용
            .cors()
        .and()
            //@CrossOrigin의 경우 인증이 없을 때만 허용
//            .addFilter(corsFilter)
            //csrf 사용하지 않음
            .csrf().disable()
            //세션 사용하지 않음
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//        .and()
//            .exceptionHandling()
//            .authenticationEntryPoint(jwtAuthenticationEntryPoint)
//            .accessDeniedHandler(new CustomAccessDeniedHandler())
//        .and()
//            .anonymous()
//            .authenticationFilter(new CustomAnonymousAuthenticationFilter("anonymousUser", jwtTokenProvider))
//            .authorities("ROLE_ANONYMOUS")
        .and()
            //폼기반 로그인 인증 사용하지 않음
            .formLogin().disable()
            .logout().disable()
            //http기반 사용하지 않음
            .httpBasic().disable()

//            .addFilter(getJwtAuthenticationFilter())
//            .addFilter(jwtAuthenticationFilter)
//            .addFilter(new JwtAuthorizationFilter(authenticationManager(), userRepository, jwtTokenProvider))
            .addFilterAfter(new JwtFilter(userRepository, jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)

//            .addFilterBefore(new JwtAuthorizationTestFilter(authenticationManager(), userRepository, jwtTokenProvider), BasicAuthenticationFilter.class)
            //oauth
            .oauth2Login()


//            로그인 완료뒤의 후처리가 필요함
        .and()

            .authorizeRequests()
                //preflight 요청의 접근권한 허용
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
//            authenticated 인증만 되면 접속이 가능
//                .antMatchers("/api/users/test").permitAll()
                .antMatchers("/", "/h2-console/**", "/favicon.ico", "/login/**", "/error").permitAll()
                .antMatchers("/api/auth/**").permitAll()
//                .antMatchers("/api/users/**").permitAll()
//                .antMatchers("/api/users").access("hasRole('ROLE_ADMIN' or hasRole('ROLE_USER'))")
//                .anyRequest().authenticated()
            //permitAll 모든 접속을 허용
            //denyAll 모든 접속을 거부

            ;

    }

}
