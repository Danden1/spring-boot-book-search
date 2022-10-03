package com.project.book.Security;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JwtTokenDTO {

    @ApiModelProperty("aceess token")
    private String accessToken;

    @ApiModelProperty("refresh token")
    private String refreshToken;
    
}
