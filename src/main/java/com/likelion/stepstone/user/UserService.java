package com.likelion.stepstone.user;

import com.likelion.stepstone.user.model.UserEntity;
import com.likelion.stepstone.user.model.UserVo;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
  private final UserRepository userRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
    this.userRepository = userRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  public void join(UserVo user) {
    UserEntity userEntity = UserEntity.toEntity(user);
    userEntity.setPassword(bCryptPasswordEncoder.encode(userEntity.getPassword()));
    userRepository.save(userEntity);
  }

  public void save(UserEntity userEntity) {
    userRepository.save(userEntity);
  }
}
