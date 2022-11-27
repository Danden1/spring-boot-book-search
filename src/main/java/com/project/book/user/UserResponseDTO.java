package com.project.book.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserResponseDTO {

    @ApiModelProperty("id")
    private Integer id;
    
    @ApiModelProperty("email")
    private String email;

    @ApiModelProperty("user name")
    private String name;

}

