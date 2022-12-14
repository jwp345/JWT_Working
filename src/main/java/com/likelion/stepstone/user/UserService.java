package com.likelion.stepstone.user;

import com.likelion.stepstone.user.model.UserEntity;
import com.likelion.stepstone.user.model.UserVo;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
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

  public UserEntity findById(String userId) {
    return userRepository.findByUserId(userId).orElseThrow(() -> new UsernameNotFoundException("아이디를 찾을 수 없습니다."));
  }
}
