package com.project.book.User;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SignupDTO {

    @ApiModelProperty("user email")
    private String email;

    @ApiModelProperty("user pwd1")
    private String pwd1;

    @ApiModelProperty("user pwd2")
    private String pwd2;

    @ApiModelProperty("user name")
    private String name;
    
}
