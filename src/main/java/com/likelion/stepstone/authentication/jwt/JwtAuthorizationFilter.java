package com.likelion.stepstone.authentication.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.likelion.stepstone.authentication.PrincipalDetails;
import com.likelion.stepstone.user.UserRepository;
import com.likelion.stepstone.user.model.UserEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 시큐리티가 fliter 가지고 있는데 그 필터중에 BasicAuthenticationFilter 라는 것이 있음.
// 권한이나 인증이 필요한 특정 주소를 요청했을 때 위 필터를 무조건 타게 되어있음.
// 만약에 권한이나 인증이 필요한 주소가 아니면 안탐.
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

  private final UserRepository userRepository;
  public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
    super(authenticationManager);
    this.userRepository = userRepository;
  }

  // 인증이나 권한이 필요한 요청이 있을 때 타게 되는 필터
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
    System.out.println("인증이나 권한이 필요한 주소 요청이 됨.");

    String jwtHeader = request.getHeader(JwtProperties.HEADER_STRING);
    System.out.println("jwtHeader : " + jwtHeader);

    // JWT Token을 검증을 해서 정상적인 사용자인지 확인

    // header 확인
    if(jwtHeader == null || !jwtHeader.startsWith(JwtProperties.TOKEN_PREFIX)) {
      chain.doFilter(request, response);
      return;
    }

    String jwtToken = request.getHeader(JwtProperties.HEADER_STRING).replace(JwtProperties.TOKEN_PREFIX, "");

    String userId =
            JWT.require(Algorithm.HMAC256(JwtProperties.SECRET)).build().verify(jwtToken).getClaim("userid").asString();

    // 서명이 정상적으로 됨.
    if(userId != null) {
      UserEntity userEntity = userRepository.findByUserId(userId).orElseThrow(() -> new UsernameNotFoundException("Cannot find user"));

      PrincipalDetails principalDetails = new PrincipalDetails(userEntity);

      // JwtToken 서명을 통해서 서명이 정상이면 Authentication 객체를 만들어준다.
      Authentication authentication
              = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

      // 강제로 시큐리티의 세션에 접근하여 Authentication 객체를 저장
      SecurityContextHolder.getContext().setAuthentication(authentication);

      chain.doFilter(request, response);
    }
  }
}
