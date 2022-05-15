package com.ex.recipeapi.controllers;

import com.ex.recipeapi.entities.Subscription;
import com.ex.recipeapi.entities.User;
import com.ex.recipeapi.repositories.SubscriptionRepository;
import com.ex.recipeapi.repositories.UserRepository;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("recipe")
public class SubscriptionController {

    Logger logger = LoggerFactory.getLogger(SubscriptionController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

//   TODO Can remove as there is a HelloController file
//     @GetMapping("/hello")
//     public String hello() {
//         return "Hello, World!";
//     }

    @GetMapping("{recipeId}")
    public String getRecipeById(@PathVariable int recipeId) {

        logger.info("SubscriptionController - getRecipeById");

        // Host url
        String host = "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/" + recipeId + "/summary";
        String charset = "application/json";

        // Headers for a request
        String x_rapidapi_host = "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com";
        String x_rapidapi_key = "9efff3fc99mshe7eb0f4e81c5bb6p1f9a9djsn3d0213e849d3";

        HttpResponse<JsonNode> response = null;

        try {
            response = Unirest.get(host)
                    .header("x-rapidapi-host", x_rapidapi_host)
                    .header("x-rapidapi-key", x_rapidapi_key)
                    .asJson();
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        int id = (response.getBody().getObject().getInt("id"));
        String title = (response.getBody().getObject().getString("title"));
        String recipe = (response.getBody().getObject().getString("summary"));

        String message = "<br>&emsp;<font size=\"5\" face=\"verdana\" color=\"purple\"><b> Recipe Title : " + title + "</b></font>"
                + "<br><br>&emsp;<font size=\"3\" face=\"verdana\" color=\"green\"><b> Recipe ID : " + id + "</b></font>"
                + "<br><br>&emsp;<font size=\"3\" face=\"verdana\" color=\"black\"><b> Recipe Instruction : </b>" + recipe + "</font>";

        return (message);
    }

    @PostMapping("subscribe/{userId}")
    public String subscribeForDailyEmail(@PathVariable int userId, @RequestBody Subscription s) {

        try {
            logger.info("SubscriptionController - subscribeForDailyEmail");
            User u = userRepository.findById(userId);

            if (u.getIsLoggedIn() == 0) {
                return "Please SIGN UP or LOG IN to subscribe into Daily Email";
            }
            if (u.getSubscriptionStatus() == 1) {
                return "You have already subscribed to Daily Email, Enjoy your subscription!";
            }
            if (u.getSubscriptionStatus() == 0) {
                u.setSubscriptionStatus(1);
                s.setEmail(u.getEmail());
                subscriptionRepository.save(s);
                return "CONGRATULATIONS! You have successfully subscribed to our specialized Daily Email.";
            } else {
                return "Error saving subscription request";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Error saving subscription request due to invalid User";
        }
    }

    @GetMapping("sendEmail/{userId}/{recipeId}")
    public String sendEmailForThisRecipe (@PathVariable int userId, @PathVariable int recipeId) {

        try {
            logger.info("SubscriptionController - sendEmailForThisRecipe");
            User u = userRepository.findById(userId);

            if (u.getIsLoggedIn() == 0) {
                return "Please SIGN UP or LOG IN to get email for this recipe";
            } else {
                String emailAddress = u.getEmail();

                // Host url
                String host = "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/" + recipeId + "/summary";
                String charset = "application/json";

                // Headers for a request
                String x_rapidapi_host = "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com";
                String x_rapidapi_key = "9efff3fc99mshe7eb0f4e81c5bb6p1f9a9djsn3d0213e849d3";

                HttpResponse<JsonNode> response = null;

                try {
                    response = Unirest.get(host)
                            .header("x-rapidapi-host", x_rapidapi_host)
                            .header("x-rapidapi-key", x_rapidapi_key)
                            .asJson();
                } catch (UnirestException e) {
                    e.printStackTrace();
                }

                int id = (response.getBody().getObject().getInt("id"));
                String title = (response.getBody().getObject().getString("title"));
                String recipe = (response.getBody().getObject().getString("summary"));

                String message = "<br>&emsp;<font size=\"5\" face=\"verdana\" color=\"purple\"><b> Recipe Title : " + title + "</b></font>"
                        + "<br><br>&emsp;<font size=\"3\" face=\"verdana\" color=\"green\"><b> Recipe ID : " + id + "</b></font>"
                        + "<br><br>&emsp;<font size=\"3\" face=\"verdana\" color=\"black\"><b> Recipe Instruction : </b>" + recipe + "</font>";

                RestTemplate restTemplate = new RestTemplate();

                String emailingAppUrl = "http://localhost:8080/email/sendEmail/" + emailAddress + "/" + message;
                ResponseEntity <String> emailResponse = restTemplate.getForEntity(emailingAppUrl, String.class);

                return "Successfully sent email with this recipe!";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return "Error sending email, Please Log In again to verify Email Address";
        }

    }

}
