package com.likelion.stepstone.user.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@ToString
@Accessors(chain=true)
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    @Setter
    private String userId;

    @Setter
    private String name;

    @Setter
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Setter
    private String role;

    private LocalDateTime createdAt;

    @Setter
    private LocalDateTime updatedAt;

    public static UserDto toDto(UserEntity entity) {
        UserDto dto = UserDto.builder()
                .userId(entity.getUserId())
                .name(entity.getName())
                .password(entity.getPassword())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();

        return dto;
    }

}
