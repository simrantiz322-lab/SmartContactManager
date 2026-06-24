package com.example.SmartContactManager.controllers;

import com.example.SmartContactManager.jwt.UtilJwt;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class LoginController {

    @Autowired
    private AuthenticationManager
            authenticationManager;

    @Autowired
    private UtilJwt utilJwt;

    @PostMapping("/do-login")
    public String login(
            @RequestParam String username,
            @RequestParam String password,
            HttpServletResponse response
    ) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        password
                )
        );

        String token =
                utilJwt.generateToken(
                        username
                );

        Cookie cookie =
                new Cookie("jwt", token);

        cookie.setHttpOnly(true);
        cookie.setPath("/");

        response.addCookie(cookie);

        return "redirect:/user/index";
    }


    @GetMapping("/logout")
    public String logout(
            HttpServletResponse response
    ) {

        Cookie cookie =
                new Cookie(
                        "jwt",
                        null
                );

        cookie.setMaxAge(0);
        cookie.setPath("/");

        response.addCookie(cookie);

        return "redirect:/signin";
    }

}
