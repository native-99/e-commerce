package com.izi.ecommerce.model;

import com.izi.ecommerce.entity.Role;
import com.izi.ecommerce.entity.User;
import lombok.Builder;
import lombok.Data;
import tools.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import tools.jackson.databind.annotation.JsonNaming;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@JsonNaming(SnakeCaseStrategy.class)
public class UserResponse implements Serializable {

    private Long userId;
    private String username;
    private String email;
    private Boolean enabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<String> roles;

    public static UserResponse fromUserAndRoles(User user, List<Role> roles) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .enabled(user.getEnabled())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .roles(roles.stream().map(Role::getName).toList())
                .build();
    }
}
