package com.ex.recipeapi.services;

import com.ex.recipeapi.entities.User;
import com.ex.recipeapi.exceptions.UserNotFoundException;
import com.ex.recipeapi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository users;

    public List<User> getAllUsers() {
        return users.findAll();
    }

    public User addUser(User user) {
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
}
