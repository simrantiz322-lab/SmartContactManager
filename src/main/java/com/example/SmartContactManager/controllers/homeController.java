package com.example.SmartContactManager.controllers;

import com.example.SmartContactManager.dao.UserRepository;
import com.example.SmartContactManager.entities.User;
import com.example.SmartContactManager.helper.Message;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class homeController {

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String home(Model m){
        m.addAttribute("title","Home page");
        return "home";
    }

    @GetMapping("/signup")
    public String signup(Model m){
        m.addAttribute("title","Signup");
        m.addAttribute("user",new User());
        return "signup";
    }

    //this is handler for registering user
    @PostMapping("/do-register")
    public String registerUser(@Valid @ModelAttribute("user") User user,BindingResult result,
                               @RequestParam(value = "agreement",defaultValue = "false") boolean agreement,
                               Model m, HttpSession session
                               ) {


        try {

            if(agreement==false){
                throw new Exception("You have not agreed terms and condition!");
            }


            if(result.hasErrors()){
                m.addAttribute("user",user);
                return "signup";
            }

            user.setRole("ROLE_USER");
            user.setEnabled(true);
            user.setImageurl("default.png");
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            userRepository.save(user);

            System.out.println(user);

            m.addAttribute("user", new User());
            session.setAttribute("message", new Message("Successfully registered." ,
                    "alert-success"));


            return "signup";

        } catch (Exception e) {
            m.addAttribute("user", user);
            session.setAttribute("message", new Message("Somenthing went wrong." + e.getMessage(),
                    "alert-danger"));

            return "signup";
        }

    }



    //handler for custom login
    @GetMapping("/signin")
    public String customlogin(){


        return "login";

    }

}
