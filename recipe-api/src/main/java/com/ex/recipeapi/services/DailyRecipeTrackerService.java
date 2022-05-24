package com.ex.recipeapi.services;

import com.ex.recipeapi.repositories.DailyRecipeTrackerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * This class will help to connect with DailyRecipeTracker repository
 */

@Service
@Transactional
public class DailyRecipeTrackerService {

    @Autowired
    private DailyRecipeTrackerRepository dailyRecipeTrackers;

}