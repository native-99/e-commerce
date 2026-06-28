package com.izi.ecommerce.service;

import com.izi.ecommerce.model.UserInfo;

public interface JwtService {

    String generateToken(UserInfo userInfo);

    boolean validateToken(String token);

    String getUsernameFromToken(String token);
}
