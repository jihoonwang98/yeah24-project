package com.prac.auth;

import com.prac.auth.provider.GoogleUserInfo;
import com.prac.auth.provider.NaverUserInfo;
import com.prac.auth.provider.OAuth2UserInfo;
import com.prac.domain.User;
import com.prac.domain.etc.Role;
import com.prac.dto.UserDTO;
import com.prac.dto.UserDTO.UserPrincipalDTO;
import com.prac.repository.user.UserRepository;
import com.prac.utils.RandomNicknameUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("userRequest.clientRegistration: " + userRequest.getClientRegistration());
        log.info("userRequest.accessToken: " + userRequest.getAccessToken());
        log.info("userRequest.additional parameters: " + userRequest.getAdditionalParameters());

        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("oauth2User attributes: " + oAuth2User.getAttributes());

        OAuth2UserInfo userInfo = null;
        if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            log.info("구글 로그인 요청");
            userInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
            log.info("네이버 로그인 요청");
            userInfo = new NaverUserInfo((Map) oAuth2User.getAttributes().get("response"));
        }


        Optional<User> optionalUser = userRepository.findUserByUsername(userInfo.getUsername());
        User userEntity = null;
        if (optionalUser.isPresent()) {
            log.info("로그인한적이 있습니다.");
            userEntity = optionalUser.get();
        } else {
            log.info("최초 로그인이므로 회원 가입을 진행합니다..");
            userEntity = User.builder()
                    .username(userInfo.getUsername())
                    .name(userInfo.getName())
                    .email(userInfo.getEmail())
                    .password(passwordEncoder.encode(userInfo.getPassword()))
                    .role(Role.valueOf(userInfo.getRole()))
                    .nickname(new RandomNicknameUtils().getNickname())
                    .provider(userInfo.getProvider())
                    .providerId(userInfo.getProviderId())
                    .build();

            userRepository.save(userEntity);
        }

        return new PrincipalDetails(new UserPrincipalDTO(userEntity), oAuth2User.getAttributes());
    }
}
