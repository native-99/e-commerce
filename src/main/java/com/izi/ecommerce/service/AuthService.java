package com.izi.ecommerce.service;

import com.izi.ecommerce.model.AuthRequest;
import com.izi.ecommerce.model.UserInfo;

public interface AuthService {
    UserInfo authenticate(AuthRequest authRequest);
}
