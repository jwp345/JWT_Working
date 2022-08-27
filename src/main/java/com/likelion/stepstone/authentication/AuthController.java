package com.likelion.stepstone.authentication;

import com.likelion.stepstone.authentication.jwt.JwtProperties;
import com.likelion.stepstone.user.UserRepository;
import com.likelion.stepstone.user.model.UserEntity;
import com.likelion.stepstone.user.model.UserVo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  private final UserRepository userRepository;

  AuthController(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository) {
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    this.userRepository = userRepository;
  }

  @GetMapping("/home")
  @ResponseBody
  public String home() {
    return "<h1>home<h1>";
  }

  @PostMapping("token")
  @ResponseBody
  public String token() {
    return "<h1>token<h1>";
  }

  @PostMapping("join")
  @ResponseBody
  public String join(@RequestBody UserVo user) {
    UserEntity userEntity = new UserEntity();
    userEntity.setName(user.getName());
    userEntity.setRoles("ROLE_USER");
    userEntity.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
    userEntity.setUserId(user.getUserId());
    userRepository.save(userEntity);

    return "join success";
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

  @GetMapping("/loginForm")
  public String loginForm() {
    return "loginForm";
  }

//  @GetMapping("/login")
//  public String login() {
//    return "loginForm";
//  }

  @GetMapping("/login-error")
  public String loginError(Model model) {
    model.addAttribute("loginError", true);
    return "loginForm";
  }
}