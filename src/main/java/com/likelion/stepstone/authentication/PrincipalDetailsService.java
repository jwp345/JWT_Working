package com.likelion.stepstone.authentication;

import com.likelion.stepstone.user.UserRepository;
import com.likelion.stepstone.user.UserService;
import com.likelion.stepstone.user.model.UserVo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PrincipalDetailsService implements UserDetailsService {

  private final UserService userService;
  PrincipalDetailsService(UserService userService) {
    this.userService = userService;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    UserVo userVo = UserVo.toVo(userService.findById(username));

    return new PrincipalDetails(userVo);
  }
}
