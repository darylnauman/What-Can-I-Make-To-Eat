package com.ex.recipeapi.controllers;

import com.ex.recipeapi.entities.User;
import com.ex.recipeapi.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity getAllUsers() {
        logger.info("UserController - getAllUsers");
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping
    public ResponseEntity addUser(@RequestBody User user) {
        try {
            logger.info("UserController - addUser");
            user = userService.addUser(user);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error saving new user");
        }
    }

    @DeleteMapping("{userId}")
    public ResponseEntity deleteUser(@PathVariable Integer userId) {
        try {
            logger.info("UserController - deleteUser");
            boolean success = userService.deleteUser(userId);
            return ResponseEntity.ok().body("User deleted");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error deleting user");
        }
    }


}
