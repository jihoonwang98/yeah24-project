package com.prac;

import com.prac.auth.PrincipalDetails;
import com.prac.domain.User;
import com.prac.domain.etc.Role;
import com.prac.dto.UserDTO;
import com.prac.dto.UserDTO.UserPrincipalDTO;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WithMockCustomUserSecurityContextFactory
        implements WithSecurityContextFactory<WithMockCustomUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        User user = User.builder()
                .username("username")
                .name("name")
                .role(Role.ROLE_USER)
                .password("asdf")
                .build();

        UserPrincipalDTO dto = new UserPrincipalDTO(user);
        dto.setId(1L);
        dto.addBooksToCart(Arrays.asList(1L, 2L, 3L));

        UserDetails principal = new PrincipalDetails(dto);

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                principal, principal.getPassword(), principal.getAuthorities());

        context.setAuthentication(authentication);
        return context;
    }
}
