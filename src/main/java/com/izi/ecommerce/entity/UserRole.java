package com.izi.ecommerce.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_roles")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRole {

    @EmbeddedId
    private UserRoleId id;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Embeddable
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserRoleId implements Serializable {

        @Column(name = "user_id")
        private Long userId;

        @Column(name = "role")
        private String role;
    }
}
