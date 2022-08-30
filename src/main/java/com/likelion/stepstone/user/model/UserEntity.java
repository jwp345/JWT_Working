package com.likelion.stepstone.user.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_cid")
    private Long cid;

    //사용자Id를 해싱한다.
    @Column(name = "user_id", length = 50, unique = true)
    @Setter
    private String userId;

    @Setter
    @Column(name = "name")
    private String name; // 실명

    @Setter
    @Column(name = "password")
    private String password;

    @Setter
    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Setter
    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Setter
    @Column(name = "login_before")
    private boolean loginBefore;

    @Setter
    @Column(name = "roles")
    private String roles;

    // enum으로 하는 방법도 있나?
    public List<String> getRoleList(){
        if(this.roles.length() > 0){
            return Arrays.asList(this.roles.split(","));
        }
        return new ArrayList<>(); // null 을 리턴 안해주기 위함.
    }

    public static UserEntity toEntity(UserDto dto) {
        UserEntity entity = UserEntity.builder()
                .userId(dto.getUserId())
                .name(dto.getName())
                .password(dto.getPassword())
                .roles(dto.getRole())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();

        return entity;
    }

    public static UserEntity toEntity(UserVo vo) {
        UserEntity entity = UserEntity.builder()
                .userId(vo.getUserId())
                .name(vo.getName())
                .password(vo.getPassword())
                .roles(vo.getRole())
                .build();
        return entity;
    }
}
