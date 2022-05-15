package com.ex.emailapi;

import com.ex.emailapi.entities.DailyRecipeTracker;
import com.ex.emailapi.repositories.DailyRecipeTrackerRepository;
import com.ex.emailapi.repositories.SubscriptionRepository;
import com.ex.emailapi.services.EmailServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * Throws an IllegalStateException when the values received as arguments for finding daily recipe id is NULL
     */
    @Test
    public void shouldThrowIllegalStateExceptionForNullDetailsForGettingDailyRecipe() {
        IllegalStateException ex = Assertions.assertThrows(IllegalStateException.class, () -> {

            emailService.getNewDailyRecipeForCurrentCustomer(null, null);
        });

        Assertions.assertEquals("Employee id or preferences can't be null", ex.getMessage(), "Method didn't throw with null values");

        ex = Assertions.assertThrows(IllegalStateException.class, () -> {

            emailService.getNewDailyRecipeForCurrentCustomer(null, "apple");
        });

        Assertions.assertEquals("Employee id or preferences can't be null", ex.getMessage(), "Method didn't throw with null values");

        ex = Assertions.assertThrows(IllegalStateException.class, () -> {

            emailService.getNewDailyRecipeForCurrentCustomer("aneeshcm18@gmail.com", null);
        });

        Assertions.assertEquals("Employee id or preferences can't be null", ex.getMessage(), "Method didn't throw with null values");

    }

    /**
     * Checks to make sure a valid ID will be received on happy path
     */
    @Test
    void shouldReturnRecipeIdForValidDetails() {
        String email = "aneeshcm18@gmail.com";
        String preferences = "fish";
        int mockTrackerId = 0;
        String mockEmail = "aneeshcm18@gmail.com";
        int mockRecipeId = 479701;
        DailyRecipeTracker mockDailyRecipeTracker = new DailyRecipeTracker();
        mockDailyRecipeTracker.setTrackerId(mockTrackerId);
        mockDailyRecipeTracker.setEmail(mockEmail);
        mockDailyRecipeTracker.setRecipeId(mockRecipeId);
        List<DailyRecipeTracker> mockDailyRecipeTrackerList = new ArrayList<DailyRecipeTracker>();
        mockDailyRecipeTrackerList.add(mockDailyRecipeTracker);

        when(dailyRecipeTrackerRepository.findAllByEmail(email)).thenReturn(mockDailyRecipeTrackerList);
        int returnedRecipeId = emailService.getNewDailyRecipeForCurrentCustomer(email, preferences);

        Assertions.assertNotNull(returnedRecipeId,"Recipe received is null");
    }

}
