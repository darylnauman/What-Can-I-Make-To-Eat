package com.ex.recipeapi;

import com.ex.recipeapi.entities.Subscription;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class SubscriptionTests {

    Subscription e = new Subscription(1, "aneeshrevatureproject1@gmail.com", "dessert");
    Subscription e1 = new Subscription();

    @Test
    void shouldGetSubscriptionId() {
        assertEquals(1, e.getSubscriptionId());
    }

    @Test
    void shouldGetEmail() {
        assertEquals("aneeshrevatureproject1@gmail.com", e.getEmail());
    }

    @Test
    void shouldGetPreferences() {
        assertEquals("dessert", e.getPreferences());
    }

    @Test
    void shouldSetSubscriptionId() {
        e1.setSubscriptionId(15);
        assertEquals(15, e1.getSubscriptionId());
    }

    @Test
    void shouldSetEmail() {
        e1.setEmail("jack@hotmail.com");
        assertEquals("jack@hotmail.com", e1.getEmail());
    }

    @Test
    void shouldSetPreferences() {
        e1.setPreferences("vegan");
        assertEquals("vegan", e1.getPreferences());
    }
}