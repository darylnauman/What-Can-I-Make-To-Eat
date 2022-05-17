package com.ex.recipeapi.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller to simply check Apache Tomcat is running and able to reach an endpoint
 */

@RestController
public class HelloController {

    Logger logger = LoggerFactory.getLogger(HelloController.class);

    @GetMapping("/hello")
    public String greeting() {
        logger.info("Reached /hello endpoint within the Recipe API");
        String s = "<html><body><center><h1>Good day! From the Recipe API hello controller.</h1></center></body></html>";
        return s;
    }
}
