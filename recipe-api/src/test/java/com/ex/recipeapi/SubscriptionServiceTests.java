package com.ex.recipeapi;

import com.ex.recipeapi.entities.Subscription;
import com.ex.recipeapi.entities.User;
import com.ex.recipeapi.repositories.SubscriptionRepository;
import com.ex.recipeapi.repositories.UserRepository;
import com.ex.recipeapi.services.SubscriptionService;
import com.ex.recipeapi.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SubscriptionServiceTests {

    private SubscriptionService subscriptionService;
    private SubscriptionRepository subscriptionRepo;
    private UserRepository userRepository;

    private UserService userService;

    Logger logger = LoggerFactory.getLogger(SubscriptionServiceTests.class);

    @BeforeEach
    public void initEachTest(){
        logger.info("Before each test");
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);

        subscriptionRepo = mock(SubscriptionRepository.class);
        subscriptionService = new SubscriptionService(subscriptionRepo, userRepository);

    }


    /**
     * Test getAllSubscribers returns all the subscribers
     */
    @Test
    public void shouldReturnAllSubscribers() {
        logger.info("Inside test - shouldReturnAllSubscribers()");
        when(subscriptionService.getAllSubscribers()).thenReturn(Collections.emptyList());
        List<Subscription> subscriptions = subscriptionService.getAllSubscribers();
        assertTrue(subscriptions.isEmpty());
    }


    /**
     * Test if sendEmailForThisRecipe() is sending emails
     */
//    @Test
//    public void shouldSendEmailForThisRecipe() {
//
//        logger.info("Inside test - shouldSendEmailForThisRecipe()");
//
//        int userId = 1;
//        int recipeId = 479102;
//        User mockUser = new User();
//        mockUser.setUserId(userId);
//        mockUser.setEmail("anju.naduth@gmail.com");
//        mockUser.setUserPassword("testPassword");
//        mockUser.setSubscriptionStatus(1);
//        mockUser.setIsLoggedIn(1);
//
//        logger.info("userid : "+mockUser.getUserId());
//        logger.info("isLoggedIn : "+mockUser.getIsLoggedIn());
//
//        when(userRepository.findById(userId)).thenReturn(mockUser);
//
//        String message = subscriptionService.sendEmailForThisRecipe(userId, recipeId);

//        Assertions.assertNotNull(message);
//
//        logger.info("Message in shouldSendEmailForThisRecipe is: "+message);
//
//        Assertions.assertEquals("Successfully sent email with this recipe!", message, "Successfully sent email with this recipe!");
//
//    }


    /**
     * Test if sendEmailForThisRecipe() is sending emails if user not logged in
     */
    @Test
    public void shouldNotSendEmailForThisRecipe_ifUserNotLoggedIn() {

        logger.info("Inside test - shouldNotSendEmailForThisRecipe()");

        int userId = 1;
        int recipeId = 479102;
        User mockUser = new User();
        mockUser.setUserId(userId);
        mockUser.setEmail("anju.naduth@gmail.com");
        mockUser.setUserPassword("testPassword");
        mockUser.setSubscriptionStatus(1);
        mockUser.setIsLoggedIn(0);

        logger.info("UserId:  "+mockUser.getUserId());
        logger.info("isLoggedIn : "+mockUser.getIsLoggedIn());

        when(userRepository.findById(userId)).thenReturn(mockUser);

        String message = subscriptionService.sendEmailForThisRecipe(userId, recipeId);

        Assertions.assertNotNull(message);

        logger.info("Message in shouldNotSendEmailForThisRecipe is: "+message);

        Assertions.assertEquals("Please SIGN UP or LOG IN to get email for this recipe", message, "SIGN UP or LOG IN to get email");

    }


    /**
     * Test for user not logged in
     */
    @Test
    public void shouldReturnLoginOrSignUp_IfIsLoggedInZero_SubscribeForDailyEmail() {

        logger.info("Inside test for user not logged in - DailyEmail");

        Subscription mock_s = new Subscription();
        mock_s.setSubscriptionId(1);
        mock_s.setEmail("test@gmail.com");
        mock_s.setPreferences("dessert");

        int userId = 1;
        User mockUser = new User();
        mockUser.setUserId(userId);
        mockUser.setEmail("test@gmail.com");
        mockUser.setUserPassword("testPassword");
        mockUser.setSubscriptionStatus(0);
        mockUser.setIsLoggedIn(0);

        when(userRepository.findById(mockUser.getUserId())).thenReturn(mockUser);
        when(subscriptionRepo.save(mock_s)).thenReturn(mock_s);
        String message = subscriptionService.subscribeForDailyEmail(userId, mock_s);
        logger.info("Message returned when IsLoggedIn is 0 : "+message);

        Assertions.assertEquals("Please SIGN UP or LOG IN to subscribe into Daily Email", message, "Please SIGN UP or LOG IN to subscribe into Daily Email");

    }

    /**
     * Test to return 'already subscribed' if SubscriptionStatus is 1
     */
    @Test
    public void shouldReturnAlreadySubscribed_IfSubscriptionStatusIsOne_SubscribeForDailyEmail() {

        logger.info("Inside test for user already subscribed - DailyEmail");

        Subscription mock_s = new Subscription();
        mock_s.setSubscriptionId(1);
        mock_s.setEmail("test@gmail.com");
        mock_s.setPreferences("dessert");

        int userId = 1;
        User mockUser = new User();
        mockUser.setUserId(userId);
        mockUser.setEmail("test@gmail.com");
        mockUser.setUserPassword("testPassword");
        mockUser.setSubscriptionStatus(1);
        mockUser.setIsLoggedIn(1);

        when(userRepository.findById(mockUser.getUserId())).thenReturn(mockUser);
        when(subscriptionRepo.save(mock_s)).thenReturn(mock_s);
        String message = subscriptionService.subscribeForDailyEmail(userId, mock_s);
        logger.info("Message returned when SubscriptionStatus is 1 : "+message);

        Assertions.assertEquals("You have already subscribed to Daily Email, Enjoy your subscription!", message, "Already subscribed  to Daily Email");

    }

    /**
     * Test for SubscriptionStatus 0
     */
    @Test
    public void shouldSaveToSubscription_IfSubscriptionStatusIsZero_SubscribeForDailyEmail() {

        logger.info("Inside test SubscriptionStatus 0 - DailyEmail");

        Subscription mock_s = new Subscription();
        mock_s.setSubscriptionId(1);
        mock_s.setEmail("test@gmail.com");
        mock_s.setPreferences("dessert");

        int userId = 1;
        User mockUser = new User();
        mockUser.setUserId(userId);
        mockUser.setEmail("test@gmail.com");
        mockUser.setUserPassword("testPassword");
        mockUser.setSubscriptionStatus(0);
        mockUser.setIsLoggedIn(1);

        when(userRepository.findById(mockUser.getUserId())).thenReturn(mockUser);
        when(subscriptionRepo.save(mock_s)).thenReturn(mock_s);
        String message = subscriptionService.subscribeForDailyEmail(userId, mock_s);
        logger.info("Message returned when SubscriptionStatus is 0 : "+message);

        Assertions.assertEquals("CONGRATULATIONS! You have successfully subscribed to our specialized Daily Email.", message, "You have successfully subscribed to our specialized Daily Email");

    }


    /**
     * Test for subscription status 1 in unsubscribeFromDailyEmail()
     */
    @Test
    public void shouldDeleteFromSubscription_IfSubscriptionStatusIsOne_unsubscribeFromDailyEmail() {

        logger.info("Inside test SubscriptionStatus 1 - unsubscribeFromDailyEmail");

        Subscription mock_s = new Subscription();
        mock_s.setSubscriptionId(1);
        mock_s.setEmail("test@gmail.com");
        mock_s.setPreferences("dessert");

        int userId = 1;
        User mockUser = new User();
        mockUser.setUserId(userId);
        mockUser.setEmail("test@gmail.com");
        mockUser.setUserPassword("testPassword");
        mockUser.setSubscriptionStatus(1);
        mockUser.setIsLoggedIn(1);

        when(userRepository.findById(mockUser.getUserId())).thenReturn(mockUser);
        when(userRepository.findByEmail(mockUser.getEmail())).thenReturn(mockUser);
        String message = subscriptionService.unsubscribeFromDailyEmail(userId);
        logger.info("Message returned after Unsubscribe when SubscriptionStatus is 1 : "+message);

        Assertions.assertEquals("You have successfully unsubscribed from Daily Email.", message, "You have successfully unsubscribed from Daily Email.");

    }


    /**
     * Test for user not loggedIn in unsubscribeFromDailyEmail()
     */
    @Test
    public void shouldReturnLoginOrSignUp_IfIsLoggedInZero_unsubscribeFromDailyEmail() {

        logger.info("Inside test user not loggedIn - unsubscribeFromDailyEmail");

        Subscription mock_s = new Subscription();
        mock_s.setSubscriptionId(1);
        mock_s.setEmail("test@gmail.com");
        mock_s.setPreferences("dessert");

        int userId = 1;
        User mockUser = new User();
        mockUser.setUserId(userId);
        mockUser.setEmail("test@gmail.com");
        mockUser.setUserPassword("testPassword");
        mockUser.setSubscriptionStatus(0);
        mockUser.setIsLoggedIn(0);

        when(userRepository.findById(mockUser.getUserId())).thenReturn(mockUser);
        when(subscriptionRepo.save(mock_s)).thenReturn(mock_s);
        String message = subscriptionService.unsubscribeFromDailyEmail(userId);
        logger.info("Message returned in Unsubscribe when IsLoggedIn is 0 : "+message);

        Assertions.assertEquals("Please SIGN UP or LOG IN to unsubscribe from Daily Email", message, "Please SIGN UP or LOG IN to unsubscribe from Daily Email");

    }


    /**
     * Test for subscription status 0 in unsubscribeFromDailyEmail()
     */
    @Test
    public void shouldReturnAlreadySubscribed_IfSubscriptionStatusIsOne_unsubscribeFromDailyEmail() {

        logger.info("Inside test for subscription status 0 - unsubscribeFromDailyEmail");

        Subscription mock_s = new Subscription();
        mock_s.setSubscriptionId(1);
        mock_s.setEmail("test@gmail.com");
        mock_s.setPreferences("dessert");

        int userId = 1;
        User mockUser = new User();
        mockUser.setUserId(userId);
        mockUser.setEmail("test@gmail.com");
        mockUser.setUserPassword("testPassword");
        mockUser.setSubscriptionStatus(0);
        mockUser.setIsLoggedIn(1);

        when(userRepository.findById(mockUser.getUserId())).thenReturn(mockUser);
        when(subscriptionRepo.save(mock_s)).thenReturn(mock_s);
        String message = subscriptionService.unsubscribeFromDailyEmail(userId);
        logger.info("Message returned in Unsubscribe when SubscriptionStatus is 1 : "+message);

        Assertions.assertEquals("You have already unsubscribed from Daily Email!", message, "You have already unsubscribed from Daily Email!");

    }


    /**
     * Test if getRecipeById() returns a recipe for valid id
     */
    @Test
    public void shouldGetRecipeForValidId() {

        String message = subscriptionService.getRecipeById(479701);

        logger.info("Message in shouldGetRecipeForValidId is: "+message);

        Assertions.assertNotNull(message);

    }


    /**
     * Test to get recipe by giving ingredients in hand
     */
    @Test
    public void shouldGetRecipeByIngredients() {

        String message = subscriptionService.getRecipeByIngredients("sugar,egg,flour");

        logger.info("Message in getRecipeByIngredients is: "+message);

        Assertions.assertNotNull(message);

    }


}
