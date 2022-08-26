package com.likelion.stepstone.user;

import com.likelion.stepstone.user.model.UserEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  private final PasswordEncoder passwordEncoder;

  UserService(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  public UserEntity login(String userId, String password) {

  }
}
