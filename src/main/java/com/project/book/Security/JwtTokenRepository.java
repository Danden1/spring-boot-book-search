package com.project.book.Security;

import org.springframework.data.jpa.repository.JpaRepository;



public interface JwtTokenRepository extends JpaRepository<JwtToken, Integer>{
    JwtToken findByaccessToken(String accessToken);
    JwtToken findByrefreshToken(String refreshToken);
}
