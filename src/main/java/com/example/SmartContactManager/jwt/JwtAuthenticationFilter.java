package com.example.SmartContactManager.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.validator.constraints.CodePointLength;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    @Autowired
    private UtilJwt utilJwt;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //get jwt
        //Bearer
        //validate


/*
        String requestTokenHeader= request.getHeader("Authorization");
        String username=null;
        String jwtToken=null;

        if(requestTokenHeader!=null && requestTokenHeader.startsWith("Bearer ")){

            jwtToken=requestTokenHeader.substring(7);

            try{

                username=utilJwt.extractUsername(jwtToken);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

 */

        Cookie[] cookies =
                request.getCookies();

        String jwtToken = null;

        if (cookies != null) {

            for (Cookie cookie :
                    cookies) {

                if ("jwt".equals(
                        cookie.getName()
                )) {

                    jwtToken =
                            cookie.getValue();

                    break;
                }
            }
        }

        String username = null;

        if (jwtToken != null) {
            try {
                username = utilJwt.extractUsername(jwtToken);
            } catch (ExpiredJwtException e) {

                System.out.println("JWT Token Expired");

                // Remove expired cookie
                Cookie cookie = new Cookie("jwt", null);
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);

            } catch (Exception e) {
                System.out.println("Invalid JWT: " + e.getMessage());
            }
        }


       /* if (jwtToken != null) {

            username =
                    utilJwt.extractUsername(
                            jwtToken
                    );
        }


            //security


            if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null){

                UserDetails userDetails= this.userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=
                        new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());

                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);


            }else {
                System.out.println("Token is not validated");
            }




        filterChain.doFilter(request,response);

 */

        if (username != null
                && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails =
                    userDetailsService.loadUserByUsername(username);

            if (utilJwt.validateToken(jwtToken, userDetails)) {

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authentication.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);

                System.out.println("User authenticated: " + username);
            } else {
                System.out.println("JWT validation failed.");
            }
        }

        filterChain.doFilter(request, response);



    }


}
