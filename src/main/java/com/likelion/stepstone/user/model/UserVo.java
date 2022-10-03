package com.likelion.stepstone.user.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

// 이건 원본대로 쓸거임
@Builder
@Data
public class UserVo {

    private final String name;

    private final String userId;

    private final String role;

    private final String password;

    public static UserVo toVo(UserEntity userEntity) {
        UserVo toVo = UserVo.builder()
                .userId(userEntity.getUserId())
                .name(userEntity.getName())
                .role(userEntity.getRoles())
                .password(userEntity.getPassword())
                .build();

        return toVo;
    }
}
