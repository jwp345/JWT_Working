package com.likelion.stepstone.config;

import com.likelion.stepstone.authentication.OAuth2SuccessHandler;
import com.likelion.stepstone.authentication.PrincipalOauth2UserService;
import com.likelion.stepstone.authentication.jwt.JwtAuthenticationFilter;
import com.likelion.stepstone.authentication.jwt.JwtAuthorizationFilter;
import com.likelion.stepstone.user.UserRepository;
import org.apache.tomcat.util.http.LegacyCookieProcessor;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final CorsConfig corsConfig;
  private final UserRepository userRepository;

  private final PrincipalOauth2UserService principalOauth2UserService;

  private final OAuth2SuccessHandler oAuth2SuccessHandler;

  SecurityConfig(CorsConfig corsConfig, UserRepository userRepository, PrincipalOauth2UserService principalOauth2UserService, OAuth2SuccessHandler oAuth2SuccessHandler) {
    this.corsConfig = corsConfig;
    this.userRepository = userRepository;
    this.principalOauth2UserService = principalOauth2UserService;
    this.oAuth2SuccessHandler = oAuth2SuccessHandler;
  }

//  @Bean
//  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//    return authenticationConfiguration.getAuthenticationManager();
//  }

  @Bean
  public WebServerFactoryCustomizer<TomcatServletWebServerFactory> cookieProcessorCustomizer() {
    return (serverFactory) -> serverFactory.addContextCustomizers(
            (context) -> context.setCookieProcessor(new LegacyCookieProcessor()));
  }
  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .formLogin().disable()
            .httpBasic().disable() // httpBasic (헤더에 ID, PW를 쿠키로 보내는 방식을 안쓰겠다.) 대신 Bearer
            .apply(new MyCustomDsl())
            .and()
            .authorizeRequests(auth -> auth.antMatchers("/api/v1/user/**")
                    .access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                    .antMatchers("/api/v1/manager/**")
                    .access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                    .antMatchers("/api/v1/admin/**")
                    .access("hasRole('ROLE_ADMIN')")
                    .anyRequest().permitAll())
            .oauth2Login(oauth -> oauth.loginPage("/loginForm")
                    .successHandler(oAuth2SuccessHandler)
                    .userInfoEndpoint()
                    .userService(principalOauth2UserService))
            .build();
  }

  public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {
    @Override
    public void configure(HttpSecurity http) throws Exception {
      AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
      http
              .addFilter(corsConfig.corsFilter())
              .addFilter(new JwtAuthenticationFilter(authenticationManager))
              .addFilter(new JwtAuthorizationFilter(authenticationManager, userRepository));
    }
  }

}
