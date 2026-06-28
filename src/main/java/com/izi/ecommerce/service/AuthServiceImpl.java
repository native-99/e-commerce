package com.izi.ecommerce.service;

import com.izi.ecommerce.common.errors.InvalidPasswordException;

import com.izi.ecommerce.model.AuthRequest;
import com.izi.ecommerce.model.UserInfo;
import com.izi.ecommerce.repository.RoleRepository;
import com.izi.ecommerce.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j

public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;

    @Override
    public UserInfo authenticate(AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),
                            authRequest.getPassword()
                    )
            );

            UserInfo user = (UserInfo) authentication.getPrincipal();
            return user;
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new InvalidPasswordException("Invalid username or password");
        }
    }
}