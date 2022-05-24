package com.ex.recipeapi;

import com.ex.recipeapi.entities.DailyRecipeTracker;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class DailyRecipeTrackerTests {

    DailyRecipeTracker e = new DailyRecipeTracker(1, "nimmie10@gmail.com", 1016711);
    DailyRecipeTracker e1 = new DailyRecipeTracker();

    @Test
    void shouldGetTrackerId() {
        assertEquals(1, e.getTrackerId());
    }

    @Test
    void shouldGetEmail() {
        assertEquals("nimmie10@gmail.com", e.getEmail());
    }

    @Test
    void shouldGetRecipeId() {
        assertEquals(1016711, e.getRecipeId());
    }

    @Test
    void shouldSetTrackerId() {
        e1.setTrackerId(36);
        assertEquals(36, e1.getTrackerId());
    }

    @Test
    void shouldSetEmail() {
        e1.setEmail("jack@hotmail.com");
        assertEquals("jack@hotmail.com", e1.getEmail());
    }

    @Test
    void shouldSetRecipeId() {
        e1.setRecipeId(4632);
        assertEquals(4632, e1.getRecipeId());
    }
}