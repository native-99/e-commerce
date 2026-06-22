package com.izi.ecommerce.repository;

import com.izi.ecommerce.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByName(String name);


    @Query(value = """
        SELECT r.* FROM roles r
        JOIN user_roles ur ON ur.role = r.name
        JOIN users u ON ur.user_id = u.user_id
        WHERE u.user_id = :userId
    """, nativeQuery = true)
    List<Role> findByUserId(Long userId);
}
