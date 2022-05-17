package com.ex.recipeapi;

import com.ex.recipeapi.controllers.UserController;
import com.ex.recipeapi.dtos.LoginDTO;
import com.ex.recipeapi.entities.User;
import com.ex.recipeapi.repositories.UserRepository;
import com.ex.recipeapi.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTests {

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository users;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldAddUser() throws Exception {
        User mockUser = new User();
        mockUser.setUserId(1);
        mockUser.setEmail("frank@gmail.com");
        mockUser.setUserPassword("password");
        mockUser.setSubscriptionStatus(0);
        mockUser.setIsLoggedIn(0);

        when(userService.addUser(any())).thenReturn(mockUser);
        mockMvc.perform(post("/api/users")
                .contentType("application/json")
                .content("{\"email\": \"frank@gmail.com\", \"userPassword\": \"password\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldLogIn() throws Exception {

        when(userService.login(any())).thenReturn(true);
        mockMvc.perform(get("/login")
                .contentType("application/json")
                .content("{\"email\": \"frank@gmail.com\", \"userPassword\": \"password\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldNotLogIn() throws Exception {

        when(userService.login(any())).thenReturn(false);
        mockMvc.perform(get("/login")
                        .contentType("application/json")
                        .content("{\"email\": \"frank@gmail.com\", \"userPassword\": \"password\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldLogOut() throws Exception {

        when(userService.logout(any())).thenReturn(true);
        mockMvc.perform(get("/logout")
                        .contentType("application/json")
                        .content("{\"email\": \"frank@gmail.com\"}"))
                .andExpect(status().isOk()).andExpect(content().string("User logged out"));
    }

}
