package com.ex.recipeapi.dtos;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogoutDTO {

    /**
     * Data Transfer Object for Logout
     */
    private String email;
}
