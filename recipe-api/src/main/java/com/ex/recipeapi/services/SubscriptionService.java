package com.ex.recipeapi.services;

import com.ex.recipeapi.controllers.SubscriptionController;
import com.ex.recipeapi.entities.Subscription;
import com.ex.recipeapi.entities.User;
import com.ex.recipeapi.repositories.SubscriptionRepository;
import com.ex.recipeapi.repositories.UserRepository;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Transactional
public class SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptions;

    @Autowired
    private UserRepository userRepository;

    Logger logger = LoggerFactory.getLogger(SubscriptionController.class);


    public SubscriptionService(SubscriptionRepository subscriptions, UserRepository userRepository) {
        this.subscriptions = subscriptions;
        this.userRepository = userRepository;
    }


    /**
     * @return - all the subscribers from Subscription table
     */

    public List <Subscription> getAllSubscribers() {
        return subscriptions.findAll();
    }

    /**
     * If a user wants to subscribe to Daily Email, this method will add the user into Subscription table
     * @param userId - unique identifier for User
     * @param s - Subscription entity
     * @return - a string that tells whether or not Subscription is successful
     */

    public String subscribeForDailyEmail (int userId, Subscription s) {

        logger.info("SubscriptionService - subscribeForDailyEmail"+userId);
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
            subscriptions.save(s);
            return "CONGRATULATIONS! You have successfully subscribed to our specialized Daily Email.";
        } else {
            return "Error saving subscription request";
        }
    }

    /**
     * If a user wants to unsubscribe from Daily Email, this method will remove the user from Subscription table
     * @param userId - unique identifier for User
     * @return - a string that tells whether or not Unsubscription is successful
     */

    public String unsubscribeFromDailyEmail (int userId) {

        logger.info("SubscriptionService - unsubscribeFromDailyEmail");
        User u = userRepository.findById(userId);

        if (u.getIsLoggedIn() == 0) {
            return "Please SIGN UP or LOG IN to unsubscribe from Daily Email";
        }
        if (u.getSubscriptionStatus() == 0) {
            return "You have already unsubscribed from Daily Email!";
        }
        if (u.getSubscriptionStatus() == 1) {
            u.setSubscriptionStatus(0);
            String email = u.getEmail();
            Subscription subscriber = subscriptions.findByEmail(email);
            logger.info("subscriber : "+subscriber);
            subscriptions.delete(subscriber);
            return "You have successfully unsubscribed from Daily Email.";
        } else {
            return "Error deleting Subscription";
        }
    }

    /**
     * If a user requests to get the recipe in the moment, this method will send the recipe to his email
     * @param userId - unique identifier for User
     * @param recipeId - unique identifier for recipe from the API
     * @return - a string that tells whether or not the email was sent successfully
     */

    public String sendEmailForThisRecipe (int userId, int recipeId) {

            logger.info("SubscriptionService - sendEmailForThisRecipe");
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

                RestTemplate restTemplate = new RestTemplate();

                String emailApiUrl = System.getenv("EMAIL_API_URL");
                String emailingAppUrl = emailApiUrl + "/email/sendEmail/" + emailAddress + "/" + id;

                ResponseEntity<String> emailResponse = restTemplate.getForEntity(emailingAppUrl, String.class);

                return "Successfully sent email with this recipe!";
            }
    }

    /**
     * User can get a particular recipe using recipe ID
     * @param recipeId - unique identifier for recipe
     * @return - a recipe with instructions and image
     */

    public String getRecipeById(int recipeId) {

        logger.info("SubscriptionService - getRecipeById " + recipeId);

        // Host url
        String host = "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/" + recipeId + "/information";
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

        logger.info("recipe id : " + response.getBody().getObject().getInt("id"));
        logger.info("recipe title : " + response.getBody().getObject().getString("title"));

        String message=("<html><body>"
                + "<br>&emsp;<font size=\"5\" face=\"verdana\" color=\"purple\"><b> Recipe Title : "
                + response.getBody().getObject().getString("title") + "</b></font>"
                + "<br><br>&emsp;<font size=\"3\" face=\"verdana\" color=\"green\"><b> Recipe ID : "
                + response.getBody().getObject().getInt("id") + "</b></font>"
                + "<br><br>&emsp;<font size=\"2\" face=\"verdana\" color=\"black\"><b> Instructions : </b>"
                + response.getBody().getObject().getString("instructions") + "</font>"
                + "<br><br>&emsp;<img src=\'"+response.getBody().getObject().getString("image")+"\'/>"
                + "<br><br>&emsp;<font size=\"3\" face=\"verdana\" color=\"black\"><b> Servings : "
                + response.getBody().getObject().getInt("servings") + "</b></font>"
                + "<br><br>&emsp;<font size=\"3\" face=\"verdana\" color=\"black\"><b> Total Time : "
                + response.getBody().getObject().getInt("readyInMinutes") + " " + " minutes</b></font>"
                + "<br><br>&emsp;<font size=\"2\" face=\"verdana\" color=\"black\"><b> Additional Information: </b>"
                + response.getBody().getObject().getString("summary") + "</font>"
                +"</body></html>");

        return (message);
    }

    /**
     * User can search for a recipe with the ingredients in hands
     * @param ingredientsInHand - comma-separated string such as flour,egg,sugar
     * @return - three recipes (title, ID, image, usedIngredientCount, missedIngredientCount) to choose from
     */

    public String getRecipeByIngredients(String ingredientsInHand) {

        logger.info("SubscriptionService - getRecipeByIngredients");

        // Host url
        String host = "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/findByIngredients?ingredients=" + ingredientsInHand + "&number=3&ignorePantry=true&ranking=1";
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

        String jsonString = null;
        JSONObject obj = new JSONObject();

        String message = "<html><body><center><h2>You have 3 Recipe IDs to choose from</h2>";

        for (int i=0; i<3; i++) {

            jsonString = response.getBody().getArray().get(i).toString();
            obj = new JSONObject(jsonString);

            logger.info("recipe id : " + obj.getInt("id"));
            logger.info("recipe title : " + obj.getString("title"));

            message +=("<br>&emsp;&emsp;&emsp;&emsp;<font size=\"3\" face=\"verdana\" color=\"red\"><b> Suggestion : " + (i+1) + "</b></font><br>"
                    +"<br>&emsp;&emsp;&emsp;&emsp;<font size=\"2\" face=\"verdana\" color=\"purple\"><b> Recipe Title : </b>"
                    + obj.getString("title") + "</font>"
                    + "<br>&emsp;&emsp;&emsp;&emsp;<font size=\"2\" face=\"verdana\" color=\"green\"><b> Recipe ID : </b>"
                    + obj.getInt("id") + "</font><br>"
                    + "<br>&emsp;&emsp;&emsp;&emsp;&emsp;<img src=\'"+obj.getString("image")+"\'/><br>"
                    + "<br>&emsp;&emsp;&emsp;&emsp;<font size=\"2\" face=\"verdana\" color=\"black\"><b> Number of Ingredients used  : </b>"
                    + obj.getInt("usedIngredientCount") + "</font>"
                    + "<br>&emsp;&emsp;&emsp;&emsp;<font size=\"2\" face=\"verdana\" color=\"black\"><b> Number of Missed Ingredients : </b>"
                    + obj.getInt("missedIngredientCount") + "</font><br>");
        }

        message += "</center></body></html>";

        return (message);
    }
}
