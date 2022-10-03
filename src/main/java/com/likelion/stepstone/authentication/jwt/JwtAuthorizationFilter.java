package com.likelion.stepstone.authentication.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.likelion.stepstone.authentication.PrincipalDetails;
import com.likelion.stepstone.user.UserRepository;
import com.likelion.stepstone.user.model.UserEntity;
import com.likelion.stepstone.user.model.UserVo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

// 시큐리티가 fliter 가지고 있는데 그 필터중에 BasicAuthenticationFilter 라는 것이 있음.
// 권한이나 인증이 필요한 특정 주소를 요청했을 때 위 필터를 무조건 타게 되어있음.
// 만약에 권한이나 인증이 필요한 주소가 아니면 안탐.
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

  private final UserRepository userRepository;
  private final String invalid = "";

  public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
    super(authenticationManager);
    this.userRepository = userRepository;
  }

  // 인증이나 권한이 필요한 요청이 있을 때 타게 되는 필터
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
    System.out.println("인증이나 권한이 필요한 주소 요청이 됨.");

    if(request.getCookies() == null) {
      chain.doFilter(request, response);
      return;
    }

    String token = Arrays.stream(request.getCookies())
            .filter(cookie -> cookie.getName().equals(JwtProperties.HEADER_STRING)).findFirst().map(Cookie::getValue)
            .orElse(invalid).replace("value : ","");

    System.out.println("jwtToken : " + token);

    // JWT Token을 검증을 해서 정상적인 사용자인지 확인

    // header 확인
    if (token.equals(invalid)) {
      chain.doFilter(request, response);
      return;
    }

    DecodedJWT decodedJWT =
            JWT.require(Algorithm.HMAC256(JwtProperties.SECRET)).build().verify(token);

    // 서명이 정상적으로 됨.
    if (decodedJWT != null) {

      UserVo userVo = UserVo.builder()
              .userId(decodedJWT.getClaim("userid").asString())
              .name(decodedJWT.getClaim("name").asString())
              .role(decodedJWT.getClaim("role").asString())
              .build();

      PrincipalDetails principalDetails = new PrincipalDetails(userVo);

      // JwtToken 서명을 통해서 서명이 정상이면 Authentication 객체를 만들어준다.
      Authentication authentication
              = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

      // 강제로 시큐리티의 세션에 접근하여 Authentication 객체를 저장
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }
    chain.doFilter(request, response);
  }
}
