package com.ex.recipeapi;

import com.ex.recipeapi.controllers.HelloController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(HelloController.class)
public class HelloControllerTests {

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void shouldGetGreeting() throws Exception {

        mockMvc.perform(get("/hello")
                        .contentType("application/json"))
                .andExpect(content().string(containsString("Good day! From the Recipe API hello controller")));

    }






}
