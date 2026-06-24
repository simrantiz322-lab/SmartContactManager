package com.example.SmartContactManager.config;

import com.example.SmartContactManager.dao.UserRepository;
import com.example.SmartContactManager.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiveimpl implements UserDetailsService {


    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 🔍 fetch user from DB
        User user = userRepository.getUserbyUserName(username);

        // ❌ if user not found
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        // ✅ return Spring Security User object or your CustomUserDetails
        return new CustomUserDetails(user);
    }


}
