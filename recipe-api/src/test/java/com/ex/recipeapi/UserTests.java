package com.ex.recipeapi;

import com.ex.recipeapi.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserTests {

    User e = new User(1, "nimmie10@gmail.com", "nimmie10", 0, 1);
    User e1 = new User();

    @Test
    void shouldGetUserId() {
        assertEquals(1, e.getUserId());
    }

    @Test
    void shouldGetEmail() {
        assertEquals("nimmie10@gmail.com", e.getEmail());
    }

    @Test
    void shouldGetUserPassword() {
        assertEquals("nimmie10", e.getUserPassword());
    }

    @Test
    void shouldGetSubscriptionStatus() {
        assertEquals(0, e.getSubscriptionStatus());
    }

    @Test
    void shouldGetIsLoggedIn() {
        assertEquals(1, e.getIsLoggedIn());
    }

    @Test
    void shouldSetUserId() {
        e1.setUserId(25);
        assertEquals(25, e1.getUserId());
    }

    @Test
    void shouldSetEmail() {
        e1.setEmail("jack@hotmail.com");
        assertEquals("jack@hotmail.com", e1.getEmail());
    }

    @Test
    void shouldSetUserPassword() {
        e1.setUserPassword("Jack");
        assertEquals("Jack", e1.getUserPassword());
    }

    @Test
    void shouldSetSubscriptionStatus() {
        e1.setSubscriptionStatus(0);
        assertEquals(0, e1.getSubscriptionStatus());
    }
    @Test
    void shouldSetIsLoggedIn() {
        e1.setIsLoggedIn(0);
        assertEquals(0, e1.getIsLoggedIn());
    }
}