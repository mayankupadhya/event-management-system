package com.example.eventmanagement.service;

import com.example.eventmanagement.entity.User;
import com.example.eventmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.eventmanagement.dto.UserDTO;
import com.example.eventmanagement.exception.ResourceNotFoundException;
import com.example.eventmanagement.security.JwtService;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    public User addUser(User user) {
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public String loginUser(User loginData) {

        User user = userRepository.findByEmail(loginData.getEmail());

        if (user == null) {
            return "User not found";
        }

        if (!user.getPassword().equals(loginData.getPassword())) {
            return "Invalid password";
        }

        return jwtService.generateToken(user);
    }

    public UserDTO getUserDTOById(Long id) {

        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }

        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }
}