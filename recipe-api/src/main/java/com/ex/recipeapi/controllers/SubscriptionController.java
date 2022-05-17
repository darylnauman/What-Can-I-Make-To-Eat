package com.ex.recipeapi.controllers;

import com.ex.recipeapi.entities.Subscription;
import com.ex.recipeapi.repositories.SubscriptionRepository;
import com.ex.recipeapi.repositories.UserRepository;
import com.ex.recipeapi.services.SubscriptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("recipe")
public class SubscriptionController {

    Logger logger = LoggerFactory.getLogger(SubscriptionController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private SubscriptionService subscriptionService;

    /**
     * If a user wants to subscribe to Daily Email, this method will add the user into Subscription table
     * @param userId - unique identifier for User
     * @param s - Subscription entity
     * @return - a string that tells whether or not Subscription is successful
     */

    @PostMapping("subscribe/{userId}")
    public String subscribeForDailyEmail(@PathVariable int userId, @RequestBody Subscription s) {
        try {
            logger.info("SubscriptionController - subscribeForDailyEmail");
            String str = subscriptionService.subscribeForDailyEmail(userId,s);
            return str;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Error saving subscription request due to invalid User";
        }
    }

    /**
     * If a user wants to unsubscribe from Daily Email, this method will remove the user from Subscription table
     * @param userId - unique identifier for User
     * @return - a string that tells whether or not Unsubscription is successful
     */

    @DeleteMapping("unsubscribe/{userId}")
    public String unsubscribeFromDailyEmail (@PathVariable int userId) {
        try {
            logger.info("SubscriptionController - unsubscribeFromDailyEmail");
            String str1 = subscriptionService.unsubscribeFromDailyEmail(userId);
            return str1;
        } catch (Exception e) {
            e.printStackTrace();
            return "Exception occured while deleting Subscription";
        }
    }

    /**
     * If a user requests to get the recipe in the moment, this method will send the recipe to his email
     * @param userId - unique identifier for User
     * @param recipeId - unique identifier for recipe from the API
     * @return - a string that tells whether or not the email was sent successfully
     */

    @GetMapping("sendEmail/{userId}/{recipeId}")
    public String sendEmailForThisRecipe (@PathVariable int userId, @PathVariable int recipeId) {

        try {
            logger.info("SubscriptionController - sendEmailForThisRecipe");
            String str2 = subscriptionService.sendEmailForThisRecipe(userId, recipeId);
            return str2;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Error sending email, Please Sign Up or Log In again to verify Email Address";
        }
    }

    /**
     * User can get a particular recipe using recipe ID
     * @param recipeId - unique identifier for recipe
     * @return - a recipe with instructions and image
     */

    @GetMapping("{recipeId}")
    public String getRecipeById(@PathVariable int recipeId) {

        try {
            logger.info("SubscriptionController - getRecipeById");
            String str3 = subscriptionService.getRecipeById(recipeId);
            return str3;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Invalid Recipe ID";
        }
    }

    /**
     * @return - all the subscribers from Subscription table
     */

    @GetMapping("subscribers")
    public ResponseEntity getAllSubscribers() {
        try {
            logger.info("SubscriptionController - getAllSubscribers");
            List <Subscription> subscribers = subscriptionService.getAllSubscribers();
            return ResponseEntity.ok(subscribers);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * User can search for a recipe with the ingredients in hands
     * @param ingredientsInHand - comma-separated string such as flour,egg,sugar
     * @return - three recipes (title, ID, image, usedIngredientCount, missedIngredientCount) to choose from
     */

    @GetMapping("ingredients/{ingredientsInHand}")
    public String getRecipeByIngredients(@PathVariable String ingredientsInHand) {

        try {
            logger.info("SubscriptionController - getRecipeByIngredients");
            String str4 = subscriptionService.getRecipeByIngredients(ingredientsInHand);
            return str4;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Sorry! No suitable recipes found for the mentioned Ingredients";
        }
    }

}

