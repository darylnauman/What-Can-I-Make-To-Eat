package com.ex.recipeapi;

import com.ex.recipeapi.dtos.LoginDTO;
import com.ex.recipeapi.dtos.LogoutDTO;
import com.ex.recipeapi.entities.User;
import com.ex.recipeapi.exceptions.UserNotFoundException;
import com.ex.recipeapi.repositories.UserRepository;
import com.ex.recipeapi.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserServiceTests {

    private UserService userService;
    private UserRepository users;

    Logger logger = LoggerFactory.getLogger(UserServiceTests.class);

    @BeforeEach
    public void initEachTest(){
        logger.info("Initialization before test");
        users = mock(UserRepository.class);
        userService = new UserService(users);
    }

    @Test
    public void shouldReturnAllUsers() {
        when(userService.getAllUsers()).thenReturn(Collections.emptyList());
        List<User> users = userService.getAllUsers();
        assertTrue(users.isEmpty());
    }

    /**
     * UserService.addUser() method testing
     */
    @Test
    public void shouldThrowIllegalStateException_addUser() {
        User user1 = new User();
        user1.setUserPassword("password");
        user1.setEmail(null);

        User user2 = new User();
        user2.setUserPassword(null);
        user2.setEmail("frank@gmail.com");

        IllegalStateException ex = Assertions.assertThrows(IllegalStateException.class, ()-> {
            userService.addUser(user1);
        });
        Assertions.assertEquals("New user must have an email and password - cannot be null", ex.getMessage(),
                "Did not throw IllegalStateException when creating user with null email");

        ex = Assertions.assertThrows(IllegalStateException.class, ()-> {
            userService.addUser(user2);
        });
        Assertions.assertEquals("New user must have an email and password - cannot be null", ex.getMessage(),
                "Did not throw IllegalStateException when creating user with null password");
    }

    @Test
    public void shouldReturnUserWhenValidNewUserInfo_addUser() {
        User mockUser = new User();
        mockUser.setUserId(1);
        mockUser.setEmail("frank@gmail.com");
        mockUser.setUserPassword("password");
        mockUser.setSubscriptionStatus(0);
        mockUser.setIsLoggedIn(0);

        when(users.save(mockUser)).thenReturn(mockUser);
        User returnedUser = userService.addUser(mockUser);
        Assertions.assertEquals(mockUser, returnedUser);
    }

    /**
     * UserService.login() method testing
     */
    @Test
    public void shouldThrowIllegalStateException_login() {
        LoginDTO loginDTO1 = new LoginDTO("frank@gmail.com", null);
        LoginDTO loginDTO2 = new LoginDTO(null, "password");
        LoginDTO loginDTO3 = new LoginDTO(null, null);

        IllegalStateException ex = Assertions.assertThrows(IllegalStateException.class, ()-> {
           userService.login(loginDTO1);
        });
        Assertions.assertEquals("Username or password cannot be null when logging in", ex.getMessage(),
                "Method did not throw with null password");

        ex = Assertions.assertThrows(IllegalStateException.class, ()-> {
            userService.login(loginDTO2);
        });
        Assertions.assertEquals("Username or password cannot be null when logging in", ex.getMessage(),
                "Method did not throw with null email");

        ex = Assertions.assertThrows(IllegalStateException.class, ()-> {
            userService.login(loginDTO3);
        });
        Assertions.assertEquals("Username or password cannot be null when logging in", ex.getMessage(),
                "Method did not throw with null email & null password");
    }

    @Test
    public void shouldThrowUserNotFoundException_login() {

        LoginDTO loginDTO1 = new LoginDTO("aUserWithThisEmailDoesNotExist@gmail.com", "password");

        UserNotFoundException ex = Assertions.assertThrows(UserNotFoundException.class, () -> {
            userService.login(loginDTO1);
        });

        Assertions.assertEquals("User not found, please sign up", ex.getMessage(),
                "Method did not throw when trying to login a user with an email that does not exist." );
    }

    @Test
    public void shouldReturnTrueWithValidLEmailPassword_login() {
        LoginDTO loginDTO = new LoginDTO("frank@gmail.com", "password");

        User mockUser = new User();
        mockUser.setUserId(1);
        mockUser.setEmail("frank@gmail.com");
        mockUser.setUserPassword("password");
        mockUser.setSubscriptionStatus(0);
        mockUser.setIsLoggedIn(0);

        when(users.findByEmail(mockUser.getEmail())).thenReturn(mockUser);
        boolean isSuccess = userService.login(loginDTO);
        Assertions.assertTrue(isSuccess, "Login with valid credentials did not return true");
    }

    @Test
    public void shouldReturnFalseWithValidEmailWrongPassword_login() {
        LoginDTO loginDTO = new LoginDTO("frank@gmail.com", "wrongpassword");

        User mockUser = new User();
        mockUser.setUserId(1);
        mockUser.setEmail("frank@gmail.com");
        mockUser.setUserPassword("password");
        mockUser.setSubscriptionStatus(0);
        mockUser.setIsLoggedIn(0);

        when(users.findByEmail(mockUser.getEmail())).thenReturn(mockUser);
        boolean isSuccess = userService.login(loginDTO);
        Assertions.assertFalse(isSuccess, "Login with valid email but wrong password did not return false");
    }

    /**
     * UserService.logout() method testing
     */
    @Test
    public void shouldThrowIllegalStateException_logout() {
        LogoutDTO logoutDTO = new LogoutDTO(null);

        IllegalStateException ex = Assertions.assertThrows(IllegalStateException.class, ()-> {
            userService.logout(logoutDTO);
        });
        Assertions.assertEquals("Email cannot be null when logging out", ex.getMessage(),
                "Method did not throw with null email on logout");
    }

    @Test
    public void shouldThrowUserNotFoundException_logout() {
        LogoutDTO logoutDTO = new LogoutDTO("aUserWithThisEmailDoesNotExist@gmail.com");

        UserNotFoundException ex = Assertions.assertThrows(UserNotFoundException.class, ()-> {
            userService.logout(logoutDTO);
        });
        Assertions.assertEquals("User not found", ex.getMessage(), "Method did not throw user not found on logout");
    }

    @Test
    public void shouldReturnTrueWithValidLogout_logout() {
        LogoutDTO logoutDTO = new LogoutDTO("frank@gmail.com");

        User mockUser = new User();
        mockUser.setUserId(1);
        mockUser.setEmail("frank@gmail.com");
        mockUser.setUserPassword("password");
        mockUser.setSubscriptionStatus(0);
        mockUser.setIsLoggedIn(1);

        when(users.findByEmail(mockUser.getEmail())).thenReturn(mockUser);
        boolean isSuccess = userService.logout(logoutDTO);
        Assertions.assertTrue(isSuccess, "Logout with valid password and logged in status did not return true");
    }

    @Test
    public void shouldReturnFalseWithValidLogoutPasswordButNotLoggedIn_logout() {
        LogoutDTO logoutDTO = new LogoutDTO("frank@gmail.com");

        User mockUser = new User();
        mockUser.setUserId(1);
        mockUser.setEmail("frank@gmail.com");
        mockUser.setUserPassword("password");
        mockUser.setSubscriptionStatus(0);
        mockUser.setIsLoggedIn(0);

        when(users.findByEmail(mockUser.getEmail())).thenReturn(mockUser);
        boolean isSuccess = userService.logout(logoutDTO);
        Assertions.assertFalse(isSuccess, "Logout with valid password and logged out status did not return false");
    }

    /**
     * UserService.deleteUser() method testing
     */

    @Test
    public void ShouldThrowUserNotFound_deleteUser() {
        UserNotFoundException ex = Assertions.assertThrows(UserNotFoundException.class, () -> {
            userService.deleteUser(0);
        });
        Assertions.assertEquals("No user with this user id found to delete", ex.getMessage(),
                "Method did not throw when trying to delete a user with id that does not exist." );
    }


    @Test
    public void shouldDeleteUser() {

        User mockUser = new User();
        mockUser.setUserId(10);
        mockUser.setEmail("test@gmail.com");
        mockUser.setUserPassword("password");
        mockUser.setSubscriptionStatus(0);
        mockUser.setIsLoggedIn(1);

        when(users.existsById(mockUser.getUserId())).thenReturn(true);
        when(userService.deleteUser(10)).thenReturn(true);
        boolean isSuccess = userService.deleteUser(10);
        Assertions.assertFalse(isSuccess, "User Deleted");
    }


}
