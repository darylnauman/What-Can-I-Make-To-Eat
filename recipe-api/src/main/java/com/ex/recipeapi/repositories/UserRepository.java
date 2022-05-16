package com.ex.recipeapi.repositories;

import com.ex.recipeapi.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository for user data
 */

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("From User where userId = :userId")
    User findById(@Param("userId") int userId);

    User findByEmail(String email);
}
