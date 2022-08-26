package com.likelion.stepstone.authentication.model;

import lombok.Data;

@Data
public class LoginDto {
  private String userId;
  private String password;
}
