package com.ex.recipeapi.controllers;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("search")
public class UserController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }

    @GetMapping("{recipeId}")
    public void getRecipeById(@PathVariable int recipeId) throws IOException {

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/recipeId/summary")
                    .get()
                    .addHeader("X-RapidAPI-Host", "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com")
                    .addHeader("X-RapidAPI-Key", "9efff3fc99mshe7eb0f4e81c5bb6p1f9a9djsn3d0213e849d3")
                    .build();

            Response response = client.newCall(request).execute();

            System.out.println(response);
    }

}
