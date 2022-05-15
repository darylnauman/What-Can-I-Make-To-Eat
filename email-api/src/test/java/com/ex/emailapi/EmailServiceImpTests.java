package com.ex.emailapi;

import com.ex.emailapi.repositories.DailyRecipeTrackerRepository;
import com.ex.emailapi.repositories.SubscriptionRepository;
import com.ex.emailapi.services.EmailServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class EmailServiceImpTests {


    private EmailServiceImpl emailService;
    private SubscriptionRepository subscriptionRepository;
    private DailyRecipeTrackerRepository dailyRecipeTrackerRepository;

    @BeforeEach
    public void initEachTest(){
        System.out.println("Initializing before test");
        subscriptionRepository = mock(SubscriptionRepository.class);
        dailyRecipeTrackerRepository = mock(DailyRecipeTrackerRepository.class);
        emailService = new EmailServiceImpl(subscriptionRepository, dailyRecipeTrackerRepository);
        System.out.println("Done init");
    }

    //Just write test for instant email below


    //Just write test for daily email sender below

}
