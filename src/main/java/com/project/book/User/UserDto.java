package com.project.book.User;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class UserDto {
    
    private String email;

    private String name;

    private String pwd;
}
