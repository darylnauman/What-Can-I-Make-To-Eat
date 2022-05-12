package com.ex.recipeapi.entities;

import lombok.*;
import javax.persistence.*;

@Entity
@Table(name= "User")
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="userId", columnDefinition = "AUTO_INCREMENT")
    private int userId;

    private String email;
    private String userPassword;
    private int subscriptionStatus = 0;
    private int isLoggedIn = 0;
}
