package com.likelion.stepstone.authentication;

import com.likelion.stepstone.authentication.jwt.JwtProperties;
import com.likelion.stepstone.authentication.jwt.JwtTokenProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
    System.out.println("OAuth2 인증이 완료됨");
    PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

    String jwtToken = JwtTokenProvider.provide(principalDetails);

    Cookie cookie = new Cookie(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwtToken);
    cookie.setMaxAge(JwtProperties.EXPIRATION_TIME);
    cookie.setPath("/");
    response.addCookie(cookie);
    response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwtToken);
    response.sendRedirect("/oauth/login");
  }
}
