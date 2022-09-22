package com.project.book.Security;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JwtTokenDTO {

    private String accessToken;
    private String refreshToken;
    
}
