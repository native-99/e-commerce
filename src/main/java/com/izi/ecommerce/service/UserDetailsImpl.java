package com.izi.ecommerce.service;

import com.izi.ecommerce.common.errors.UserNotFoundException;
import com.izi.ecommerce.entity.Role;
import com.izi.ecommerce.entity.User;
import com.izi.ecommerce.model.UserInfo;
import com.izi.ecommerce.repository.RoleRepository;
import com.izi.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByKeyword(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));

        List<Role> roles = roleRepository.findByUserId(user.getUserId());

        return UserInfo.builder()
                .roles(roles)
                .user(user)
                .build();
    }
}
