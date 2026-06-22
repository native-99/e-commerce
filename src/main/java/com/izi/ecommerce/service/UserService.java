package com.izi.ecommerce.service;

import com.izi.ecommerce.model.UserRegisterRequest;
import com.izi.ecommerce.model.UserResponse;
import com.izi.ecommerce.model.UserUpdateRequest;

public interface UserService {
    UserResponse  register(UserRegisterRequest userRegisterRequest);
    UserResponse  findById(Long id);
    UserResponse  findByKeyword(String keyword);
    UserResponse  updateUser(Long id, UserUpdateRequest request);
    void  deleteUser(Long id);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
