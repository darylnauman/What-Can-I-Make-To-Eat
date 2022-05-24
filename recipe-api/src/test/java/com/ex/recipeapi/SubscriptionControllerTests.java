package com.ex.recipeapi;

import com.ex.recipeapi.controllers.SubscriptionController;
import com.ex.recipeapi.entities.Subscription;
import com.ex.recipeapi.repositories.SubscriptionRepository;
import com.ex.recipeapi.repositories.UserRepository;
import com.ex.recipeapi.services.SubscriptionService;
import com.ex.recipeapi.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SubscriptionController.class)
public class SubscriptionControllerTests {

    @MockBean
    private UserService userService;

    @MockBean
    private SubscriptionService subscriptionService;

    @MockBean
    private UserRepository users;

    @MockBean
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldSubscribeForDailyEmail() throws Exception {

        when(subscriptionService.subscribeForDailyEmail(anyInt(), any())).thenReturn("CONGRATULATIONS! You have successfully subscribed to our specialized Daily Email.");
        mockMvc.perform(post("/recipe/subscribe/{userId}", 95)
                .contentType("application/json")
                .content("{\"email\": \"mocking@yahoo.com\"}"))
                .andExpect(content().string("CONGRATULATIONS! You have successfully subscribed to our specialized Daily Email."));
    }

    @Test
    public void shouldUnsubscribeFromDailyEmail() throws Exception {

        when(subscriptionService.unsubscribeFromDailyEmail(anyInt())).thenReturn("You have successfully unsubscribed from Daily Email.");
        mockMvc.perform(delete("/recipe/unsubscribe/{userId}", 92)
                        .contentType("application/json")
                        .accept("application/json"))
                        .andExpect(content().string("You have successfully unsubscribed from Daily Email."));
    }

    @Test
    public void shouldSendEmailForThisRecipe() throws Exception {

        when(subscriptionService.sendEmailForThisRecipe(anyInt(),anyInt())).thenReturn("Successfully sent email with this recipe!");
        mockMvc.perform(get("/recipe/sendEmail/{userId}/{recipeId}", 91, 4632)
                        .contentType("application/json")
                        .accept("application/json"))
                        .andExpect(content().string("Successfully sent email with this recipe!"));

    }

    @Test
    public void shouldGetRecipeById() throws Exception {

        when(subscriptionService.getRecipeById (anyInt())).thenReturn("Recipe Title : ");
        mockMvc.perform(get("/recipe/{recipeId}", 4632)
                        .contentType("application/json")
                        .accept("application/json"))
                        .andExpect(content().string(containsString("Recipe Title : ")));

    }

    @Test
    public void shouldGetAllSubscribers() throws Exception {

        Subscription mockS1 = new Subscription();
        Subscription mockS2 = new Subscription();
        mockS1.setSubscriptionId(99);
        mockS1.setEmail("mocking1@yahoo.com");
        mockS1.setPreferences(null);
        mockS2.setSubscriptionId(98);
        mockS2.setEmail("mocking2@yahoo.com");
        mockS2.setPreferences(null);

        List<Subscription> subscriptionList = new ArrayList<>();
        when(subscriptionService.getAllSubscribers()).thenReturn(subscriptionList);
        mockMvc.perform(get("/recipe/subscribers")
                        .contentType("application/json")
                        .accept("application/json"))
                        .andExpect(status().isOk());

    }

    @Test
    public void shouldGetRecipeByIngredients() throws Exception {

        when(subscriptionService.getRecipeByIngredients (anyString())).thenReturn("You have 3 Recipe IDs to choose from");
        mockMvc.perform(get("/recipe/ingredients/{ingredientsInHand}", "eggs")
                        .contentType("application/json")
                        .accept("application/json"))
                .andExpect(content().string(containsString("You have 3 Recipe IDs to choose from")));

    }
}
