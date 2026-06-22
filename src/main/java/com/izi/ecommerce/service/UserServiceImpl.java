package com.izi.ecommerce.service;

import com.izi.ecommerce.common.errors.EmailAlreadyExitsException;
import com.izi.ecommerce.common.errors.InvalidPasswordException;
import com.izi.ecommerce.common.errors.RoleNotFoundException;
import com.izi.ecommerce.common.errors.UserNotFoundException;
import com.izi.ecommerce.common.errors.UsernameAlreadyExistsException;
import com.izi.ecommerce.entity.Role;
import com.izi.ecommerce.entity.User;
import com.izi.ecommerce.entity.UserRole;
import com.izi.ecommerce.model.UserRegisterRequest;
import com.izi.ecommerce.model.UserResponse;
import com.izi.ecommerce.model.UserUpdateRequest;
import com.izi.ecommerce.repository.RoleRepository;
import com.izi.ecommerce.repository.UserRepository;
import com.izi.ecommerce.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponse register(UserRegisterRequest userRegisterRequest) {
        if (userRepository.existsByUsername(userRegisterRequest.getUsername())) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }

        if (userRepository.existsByEmail(userRegisterRequest.getEmail())) {
            throw new EmailAlreadyExitsException("Email already exists");
        }

        if (!userRegisterRequest.getPassword().equals(userRegisterRequest.getPasswordConfirmation())) {
            throw new InvalidPasswordException("Password confirmation does not match");
        }

        Role defaultRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RoleNotFoundException("Default role ROLE_USER not found"));

        User user = User.builder()
                .username(userRegisterRequest.getUsername())
                .name(userRegisterRequest.getUsername())
                .email(userRegisterRequest.getEmail())
                .password(passwordEncoder.encode(userRegisterRequest.getPassword()))
                .enabled(true)
                .build();

        User savedUser = userRepository.save(user);

        userRoleRepository.save(UserRole.builder()
                .id(UserRole.UserRoleId.builder()
                        .userId(savedUser.getUserId())
                        .role(defaultRole.getName())
                        .build())
                .build());

        return UserResponse.fromUserAndRoles(savedUser, List.of(defaultRole));
    }

    @Override
    public UserResponse findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(()-> new UserNotFoundException("User not found with id"+id));

        List<Role> roles = roleRepository.findByUserId(id);
        return UserResponse.fromUserAndRoles(user,roles);
    }

    @Override
    public UserResponse findByKeyword(String keyword) {
        User user = userRepository.findByKeyword(keyword)
                .orElseThrow(()-> new UserNotFoundException("User not found with username/email"+keyword));

        List<Role> roles = roleRepository.findByUserId(user.getUserId());
        return UserResponse.fromUserAndRoles(user,roles);
    }

    @Override
    @Transactional
    public UserResponse updateUser(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(()-> new UserNotFoundException("User not found with id"+id));

        if (request.getUsername() != null && !request.getUsername().equals(user.getUsername())) {
            if (userRepository.existsByUsername(request.getUsername())) {
                throw new UsernameAlreadyExistsException("Username already exists");
            }
            user.setUsername(request.getUsername());
            user.setName(request.getUsername());
        }

        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new EmailAlreadyExitsException("Email already exists");
            }
            user.setEmail(request.getEmail());
        }

        if (request.getNewPassword() != null) {
            if (request.getCurrentPassword() == null ||
                    !passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
                throw new InvalidPasswordException("Current password is invalid");
            }

            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        }

        User savedUser = userRepository.save(user);
        List<Role> roles = roleRepository.findByUserId(savedUser.getUserId());
        return UserResponse.fromUserAndRoles(savedUser, roles);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(()-> new UserNotFoundException("User not found with id"+id));
        userRoleRepository.deleteByIdUserId(user.getUserId());
        userRepository.delete(user);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
