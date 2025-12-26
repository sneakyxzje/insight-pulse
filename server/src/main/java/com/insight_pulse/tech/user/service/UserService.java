package com.insight_pulse.tech.user.service;

import org.springframework.stereotype.Service;

import com.insight_pulse.tech.user.domain.User;
import com.insight_pulse.tech.user.domain.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;


    public User processOAuthPostLogin(String email, String name, String googleId) {
        return userRepository.findByGoogleId(googleId).orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setFullname(name);
            newUser.setGoogleId(googleId);
            return userRepository.save(newUser);
        });
    }
}