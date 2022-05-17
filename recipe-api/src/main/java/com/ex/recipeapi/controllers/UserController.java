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

    /**
     * User can login to Recipe API database if he has already signed up
     * @param loginDTO - login object
     * @return - result of login attempt (successful or unsuccessful)
     */

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

    /**
     * User will logout from Recipe API database if he has already logged in
     * @param logoutDTO - logout object
     * @return - result of logout attempt (valid or invalid)
     */

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

    /**
     * this method will show all the users in Recipe API database
     * @return - list of Users
     */

    @GetMapping("/api/users")
    public ResponseEntity getAllUsers() {
        logger.info("UserController - getAllUsers");
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Anyone can Sign Up for Recipe API to get recipes of their choice
     * @param user - entity
     * @return - if sign up is successful or not
     */

    @PostMapping("/signup")
    public ResponseEntity addUser(@RequestBody User user) {
        try {
            logger.info("UserController - addUser");
            user = userService.addUser(user);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error saving new user, Please try again!");
        }
    }

    /**
     * this method will remove a user from Recipe API database
     * @param userId - unique identifier for existing user
     * @return - if user is successfully deleted or not
     */

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
