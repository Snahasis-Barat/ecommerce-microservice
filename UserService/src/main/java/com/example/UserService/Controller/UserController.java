package com.example.UserService.Controller;


import com.example.UserService.Service.UserService;
import com.example.UserService.model.User;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService service;


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user)  {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.register(user));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }


    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {

        return service.generateToken(user);
    }

    @GetMapping("/verifyUser")
    public boolean verifyUser(@RequestHeader("Authorization")String request)
    {
        System.out.println("Authorisation header from User service "+request);
        return service.verifyUser(request);
    }

    @GetMapping("/getUsers")
    public ResponseEntity<?> getAllUsers(@RequestHeader("Authorization") String request)
    {

        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.getAllUsers(request));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/resetPassword")
    public ResponseEntity<?> forgotPassword(@RequestBody User user)
    {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.resetPassword(user));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
