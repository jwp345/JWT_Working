package com.likelion.stepstone.authentication;

import com.likelion.stepstone.authentication.jwt.JwtProperties;
import com.likelion.stepstone.authentication.jwt.JwtTokenProvider;
import com.likelion.stepstone.user.UserService;
import com.likelion.stepstone.user.model.UserEntity;
import com.likelion.stepstone.user.model.UserVo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.likelion.stepstone.authentication.CookieUtils.*;


@Controller
public class AuthController {

  private final UserService userService;

  AuthController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("join")
  public String join() {
    return "joinForm";
  }

  @PostMapping("join")
  public String join(UserVo userVo) {
    userService.join(userVo);
    return "loginForm";
  }

  // user, manager, admin 권한만 접근 가능
  @GetMapping("api/v1/user")
  public @ResponseBody Object user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
    return principalDetails;
  }

  // manager, admin 권한만 접근 가능
  @GetMapping("api/v1/manager")
  public @ResponseBody String manager() {
    return "manager";
  }

  @GetMapping("api/v1/admin")
  @ResponseBody
  public String admin() {
    return "admin";
  }

  @GetMapping("/oauth/login")
  public String oauthLogin(HttpServletRequest request, HttpServletResponse response, @AuthenticationPrincipal PrincipalDetails principalDetails) {
    UserEntity userEntity = principalDetails.getUser();

    deleteCookie(request, response, JwtProperties.HEADER_STRING);
    String jwtToken = JwtTokenProvider.provide(principalDetails);
    addStrictCookie(response, JwtProperties.HEADER_STRING, jwtToken, JwtProperties.EXPIRATION_TIME / 1000);
    if(!userEntity.isLoginBefore()) {
      return "oauthJoinForm";
    }
    return "redirect:/";
  }

  @PostMapping("/oauth/login")
  public String oauthLogin(@AuthenticationPrincipal PrincipalDetails principalDetails, @RequestParam String role) {
    UserEntity userEntity = principalDetails.getUser();
    userEntity.setRoles(role);
    userEntity.setLoginBefore(true);
    userService.save(userEntity);
    return "redirect:/";
  }

  @GetMapping("/login")
  public String login() {
    return "loginForm";
  }

  @GetMapping("/login-error")
  public String loginError(Model model) {
    model.addAttribute("loginError", true);
    return "loginForm";
  }
}