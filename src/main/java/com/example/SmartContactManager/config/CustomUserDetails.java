package com.example.SmartContactManager.config;

import com.example.SmartContactManager.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {


    private User user;

    // Constructor
    public CustomUserDetails(User user) {
        this.user=user;
    }

    // ✅ Authorities (roles/permissions)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        //user k paas jobhi authorities h vo paas krni h yahape
        SimpleGrantedAuthority simpleGrantedAuthority=new SimpleGrantedAuthority(user.getRole());

        return List.of(simpleGrantedAuthority);
    }

    // ✅ Password
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    // ✅ Username
    @Override
    public String getUsername() {
        return user.getUseremail();
    }

    // ✅ Account not expired
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // ✅ Account not locked
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // ✅ Credentials not expired
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // ✅ User enabled or not
    @Override
    public boolean isEnabled() {
        return true;
    }

}
