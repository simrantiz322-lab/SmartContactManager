package com.example.SmartContactManager.controllers;

import com.example.SmartContactManager.jwt.JwtRequest;
import com.example.SmartContactManager.jwt.JwtResponse;
import com.example.SmartContactManager.jwt.UtilJwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JwtController {


    //no need of this classs
    //class used when used postman


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UtilJwt utilJwt;

    @PostMapping("/token")
    public ResponseEntity<?> generateToken(@RequestBody JwtRequest jwtRequest){



        System.out.println(jwtRequest);


        try{

            this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(),jwtRequest.getPassword()));


        } catch (UsernameNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Bad credentials");
        }

        //fine area

        UserDetails userDetails= this.userDetailsService.loadUserByUsername(jwtRequest.getUsername());

        String token=this.utilJwt.generateToken(userDetails.getUsername());

        return ResponseEntity.ok(new JwtResponse(token));

    }


}
