package com.foodtech.foodtechstore.auth.entity;


import com.foodtech.foodtechstore.admin.model.AdminDoc;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomAdminDetails implements UserDetails {

    private String login;
    private String password;
    private Collection<? extends GrantedAuthority> grantedAuthorities; // роли пользователя


    public static CustomAdminDetails fromAdminEntityToCustomUserDetails(AdminDoc adminDoc) {
        CustomAdminDetails c = new CustomAdminDetails();
        c.login = adminDoc.getEmail();
        c.password = adminDoc.getPassword();

        return c;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
