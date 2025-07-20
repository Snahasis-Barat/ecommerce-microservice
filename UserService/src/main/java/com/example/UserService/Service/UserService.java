package com.example.UserService.Service;

import com.example.UserService.Repo.UserRepo;
import com.example.UserService.model.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private JWTService jwtService;

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    private UserRepo repo;


    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public User register(User user) throws Exception {

        User checkExistingUser=repo.findByUsername(user.getUsername());
        if(checkExistingUser!=null)
        {
            throw new Exception("User already exists");
        }
        else{
            user.setPassword(encoder.encode(user.getPassword()));
            repo.save(user);
        }

        return user;
    }

    public String verify(User user) {

        BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder();

        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(user.getUsername());
        } else {
            return "fail";
        }
    }

    public List<User> getAllUsers(HttpServletRequest request) throws Exception {
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            username = jwtService.extractUserName(token);
        }

        if (!jwtService.validateToken(token)) {
            throw new AccessDeniedException("User not authorized to view the resource");
        }
        return repo.findAll();

    }

    public String resetPassword(User user) throws Exception {
        if(repo.findByUsername(user.getUsername())!=null)
        {
            User existingUser=repo.findByUsername(user.getUsername());
            existingUser.setPassword(encoder.encode(user.getPassword()));
            repo.save(existingUser);
        }
        else{
            throw new Exception("User don't exists");
        }
        return "Password reset successfully";
    }
}
