package com.prac.auth;

import com.prac.dto.UserDTO.UserPrincipalDTO;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class PrincipalDetails implements UserDetails, OAuth2User {

    private UserPrincipalDTO user;
    private Map<String, Object> attributes;

    public PrincipalDetails(UserPrincipalDTO user) {
        this.user = user;
        this.attributes = new HashMap<>();
    }

    public void addBooksToCart(List<Long> bookIds) {
        this.user.addBooksToCart(bookIds);
    }

    public void removeBookInCart(Long bookId) {
        this.user.removeBookInCart(bookId);
    }

    public void clearAllBooksInCart() {
        this.user.clearAllBooksInCart();
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority(user.getRole().name()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
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


    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return (String) this.attributes.get("sub");
    }
}
