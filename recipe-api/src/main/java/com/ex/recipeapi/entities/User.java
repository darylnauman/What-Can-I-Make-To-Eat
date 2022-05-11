package com.ex.recipeapi.entities;

import lombok.*;
import javax.persistence.*;

@Entity
@Table
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    private String email;
    private String userPassword;
    private int isLoggedIn;
    private int subscriptionStatus;

}
