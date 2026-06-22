package com.izi.ecommerce.repository;

import com.izi.ecommerce.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, UserRole.UserRoleId> {
    void deleteByIdUserId(Long userId);
}
