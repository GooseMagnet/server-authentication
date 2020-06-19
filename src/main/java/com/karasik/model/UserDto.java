package com.karasik.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    @Setter
    @Getter
    private String id;

    @Setter
    @Getter
    private String email;

    @Setter
    @Getter
    private String name;

    @Setter
    @Getter
    private String password;

    @Setter
    @Getter
    private String salt;
}
