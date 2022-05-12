package com.ex.recipeapi.services;

import com.ex.recipeapi.repositories.DailyRecipeTrackerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DailyRecipeTrackerService {

    @Autowired
    DailyRecipeTrackerRepository dailyRecipeTrackers;

}
