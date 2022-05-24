package com.ex.recipeapi.entities;

import lombok.*;
import javax.persistence.*;

/**
 * A class to track all the recipes sent to the subscribers so that they won't get repeated recipes
 */

@Entity
@Table(name= "DailyRecipeTracker")
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class DailyRecipeTracker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="trackerId", columnDefinition = "AUTO_INCREMENT")
    private int trackerId;

    private String email;
    private int recipeId;
}
