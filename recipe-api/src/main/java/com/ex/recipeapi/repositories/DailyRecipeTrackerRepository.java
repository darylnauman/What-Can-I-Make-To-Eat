package com.ex.recipeapi.repositories;

import com.ex.recipeapi.entities.DailyRecipeTracker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for daily recipe trackers data
 */

@Repository
public interface DailyRecipeTrackerRepository extends JpaRepository<DailyRecipeTracker, Integer> {

}
