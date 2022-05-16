package com.ex.recipeapi.controllers;

import com.ex.recipeapi.dtos.LoginDTO;
import com.ex.recipeapi.dtos.LogoutDTO;
import com.ex.recipeapi.entities.User;
import com.ex.recipeapi.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @GetMapping("/login")
    public ResponseEntity login(@RequestBody LoginDTO loginDTO) {
        try {
            logger.info("UserController - /login");
            boolean isSuccess = false;
            isSuccess = userService.login(loginDTO);

            if(isSuccess) {
                return ResponseEntity.ok().body("User successfully logged in");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.warn("UserController - catch block for login");
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error logging in user");
        }
    }

    @GetMapping("/logout")
    public ResponseEntity logout(@RequestBody LogoutDTO logoutDTO) {
        try {
            logger.info("UserController - /logout");
            boolean isSuccess = false;
            isSuccess = userService.logout(logoutDTO);

            if(isSuccess) {
                return ResponseEntity.ok().body("User logged out");
            } else {
                return ResponseEntity.ok().body("User was not logged in - no action taken");
            }
        } catch (Exception e) {
            logger.warn("UserController - catch block for logout");
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error logging out user");
        }
    }

    @GetMapping("/api/users")
    public ResponseEntity getAllUsers() {
        logger.info("UserController - getAllUsers");
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/api/users")
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

    @DeleteMapping("/api/users/{userId}")
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
