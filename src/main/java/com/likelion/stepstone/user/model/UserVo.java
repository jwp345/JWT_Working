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

    private final String password;

//    public static UserVo toVo(UserDto dto) {
//        UserVo toVo = UserVo.builder()
//                .userId(dto.getUserId())
//                .name(dto.getName())
//                .role(dto.getRole())
//                .build();
//
//        return toVo;
//    }
}
