package com.ex.emailapi.repositories;


import com.ex.emailapi.entities.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Subscribed users
 */

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Integer> {

}
