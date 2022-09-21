package com.project.book.Security;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JwtTokenRepository extends JpaRepository<JwtToken, Integer>{
    JwtToken findbyaccessToken(String accessToken);
    JwtToken findbyrefreshToken(String refreshToken);
}
