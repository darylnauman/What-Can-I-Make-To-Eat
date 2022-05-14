package com.ex.emailapi.repositories;

import com.ex.emailapi.entities.DailyRecipeTracker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for daily recipe trackers data
 */

@Repository
public interface DailyRecipeTrackerRepository extends JpaRepository<DailyRecipeTracker, Integer> {

}
