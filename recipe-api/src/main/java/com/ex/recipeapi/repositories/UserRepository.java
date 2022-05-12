package com.ex.recipeapi.repositories;

import com.ex.recipeapi.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for user data
 */

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

}
