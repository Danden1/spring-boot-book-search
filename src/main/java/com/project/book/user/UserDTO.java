package com.project.book.user;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class UserDTO{
    
    @ApiModelProperty("email")
    private String email;

    @ApiModelProperty("password")
    private String pwd;

}
