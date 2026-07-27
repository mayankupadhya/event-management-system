package com.example.eventmanagement.controller;

import com.example.eventmanagement.entity.User;
import com.example.eventmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.example.eventmanagement.entity.Registration;
import com.example.eventmanagement.service.UserService;
import com.example.eventmanagement.dto.UserDTO;
import java.util.List;
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/add")
    public User addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable Long id) {
        return userService.getUserDTOById(id);
    }
    @GetMapping("/{id}/registrations")
    public List<Registration> getUserRegistrations(@PathVariable Long id) {

        User user = userService.getUserById(id);

        if (user == null) {
            return null;
        }

        return user.getRegistrations();
    }

    @PostMapping("/login")
    public String loginUser(@RequestBody User user) {
        return userService.loginUser(user);
    }


}