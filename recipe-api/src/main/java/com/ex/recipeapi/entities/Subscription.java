package com.ex.recipeapi.entities;

import lombok.*;
import javax.persistence.*;

@Entity
@Table(name= "Subscription")
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="subscriptionId", columnDefinition = "AUTO_INCREMENT")
    private int subscriptionId;

    private String email;
    private String preferences;
}
