package com.ex.recipeapi.services;

import com.ex.recipeapi.controllers.UserController;
import com.ex.recipeapi.dtos.LoginDTO;
import com.ex.recipeapi.dtos.LogoutDTO;
import com.ex.recipeapi.entities.User;
import com.ex.recipeapi.exceptions.UserNotFoundException;
import com.ex.recipeapi.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service layer related to users
 */

@Service
@Transactional
public class UserService {

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository users;

    public UserService(UserRepository users) {
        this.users = users;
    }

    public List<User> getAllUsers() {
        return users.findAll();
    }

    public User addUser(User user) throws IllegalStateException {

        if (user.getEmail() == null || user.getUserPassword() == null) {
            throw new IllegalStateException("New user must have an email and password - cannot be null");
        }

        return users.save(user);
    }

    public boolean deleteUser(Integer userId) {
        if (users.existsById(userId)) {
            users.deleteById(userId);
            if (!users.existsById(userId)) {
                return true;
            } else {
                return false;
            }
        } else {
            throw new UserNotFoundException("No user with this user id found to delete");
        }
    }

    public boolean login(LoginDTO loginDTO) throws UserNotFoundException, IllegalStateException {

        boolean isSuccess = false;

        if (loginDTO.getEmail() == null || loginDTO.getUserPassword() == null) {
            throw new IllegalStateException("Username or password cannot be null when logging in");
        }

        User user = users.findByEmail(loginDTO.getEmail());
        System.out.println(user);

        if (user == null) {
            throw new UserNotFoundException("User not found, Please Sign Up first");
        }

        if (user.getUserPassword().equals(loginDTO.getUserPassword())) {
            logger.info("Entered password matches user's password in the database, logging them in...");
            isSuccess = true;
            user.setIsLoggedIn(1);
        } else {
            logger.info("Entered password does not match user's password in the database");
        }
        return isSuccess;
    }

    public boolean logout(LogoutDTO logoutDTO) {
        boolean isSuccess = false;

        if (logoutDTO.getEmail() == null) {
            throw new IllegalStateException("Email cannot be null when logging out");
        }

        User user = users.findByEmail(logoutDTO.getEmail());

        if (user == null) {
            throw new UserNotFoundException("User not found");
        }

        if (user.getIsLoggedIn() == 1) {
            logger.info("User is logged in, logging them out...");
            isSuccess = true;
            user.setIsLoggedIn(0);
        } else {
            logger.info("User was not logged in");
        }
        return isSuccess;
    }
}
