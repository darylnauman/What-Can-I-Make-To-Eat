package com.ex.recipeapi.repositories;

import com.ex.recipeapi.entities.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository for Subscribed users
 */

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {

    @Query("From Subscription where email = :email")
    Subscription findByEmail(@Param("email") String email);
}
