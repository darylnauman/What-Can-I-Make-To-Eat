package com.ex.recipeapi.dtos;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginDTO {

    /**
     * Data Transfer Object for Login
     */
    private String email;
    private String userPassword;
}
