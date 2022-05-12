package com.ex.recipeapi.entities;

import lombok.*;
import javax.persistence.*;

@Table
@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class DailyRecipeTracker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int trackerId;

    private String email;
    private int recipeId;
}
