package com.prac.auth.provider;

public interface OAuth2UserInfo {

    String getProviderId();
    String getProvider();
    String getEmail();
    String getName();
    String getUsername();
    String getPassword();
    String getRole();
}
