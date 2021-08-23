package com.prac.dto;

import com.prac.domain.User;
import com.prac.domain.etc.Address;
import com.prac.domain.etc.Gender;
import com.prac.domain.etc.Role;
import com.prac.utils.ImageUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserDTO {

    @Data
    public static class UserReviewDTO {
        private Long userId;
        private String nickname;
        private String profileImg;

        public UserReviewDTO(User user) {
            this.userId = user.getId();
            this.nickname = user.getNickname();
            this.profileImg = user.getProfileImg();
        }
    }

    @Data
    public static class UserPrincipalDTO {
        private Long id;
        private String username;
        private String password;
        private String nickname;
        private String name;
        private Gender gender;
        private Address address;
        private String email;
        private Role role;
        private String provider;
        private String providerId;

        private Set<Long> carts;

        public void addBooksToCart(List<Long> bookIds) {
            this.carts.addAll(bookIds);
        }

        public void removeBookInCart(Long bookId) {
            this.carts.remove(bookId);
        }

        public UserPrincipalDTO(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.password = user.getPassword();
            this.nickname = user.getNickname();
            this.name = user.getName();
            this.gender = user.getGender();
            this.address = user.getAddress();
            this.email = user.getEmail();
            this.role = user.getRole();
            this.provider = user.getProvider();
            this.providerId = user.getProviderId();
            this.carts = new HashSet<>();
        }

        public void clearAllBooksInCart() {
            this.carts.clear();
        }
    }

    @Data
    public static class UserRegisterDTO {

        @NotBlank(message = "아이디를 입력해야 합니다.")
        private String username;

        @NotBlank(message = "패스워드를 입력해야 합니다.")
        private String password;

        @NotBlank(message = "닉네임을 입력해야 합니다.")
        private String nickname;

        @NotBlank(message = "이름을 입력해야 합니다.")
        private String name;

        @NotNull(message = "성별을 선택해야 합니다.")
        private Gender gender;

        @Valid
        private Address address;

        @NotBlank(message = "이메일을 입력해야 합니다.")
        @Email(message = "이메일을 입력해야 합니다.")
        private String email;

        private MultipartFile profileImg;


        public User toEntity(String imgSrc) {
            return User.builder()
                    .username(username)
                    .password(password)
                    .nickname(nickname)
                    .name(name)
                    .gender(gender)
                    .address(address)
                    .email(email)
                    .provider("basic-join")
                    .profileImg(imgSrc)
                    .role(Role.ROLE_USER)
                    .build();
        }

    }

    @Data
    @NoArgsConstructor
    public static class UserDetailDTO {
        private String username;
        private String password;
        private String nickname;
        private String name;
        private Address address;
        private String email;
        private String profileImg;

        public UserDetailDTO(User user) {
            this.username = user.getUsername();
            this.password = user.getPassword();
            this.nickname = user.getNickname();
            this.name = user.getName();
            this.address = user.getAddress();
            this.email = user.getEmail();
            this.profileImg = user.getProfileImg();
        }
    }

    @Data
    @NoArgsConstructor
    public static class UserModifyDTO {

        @NotBlank(message = "패스워드를 입력해야 합니다.")
        private String password;

        @NotBlank(message = "닉네임을 입력해야 합니다.")
        private String nickname;

        @Valid
        private Address address;

        private MultipartFile profileImg;
    }


    @Data
    public static class UserWishlistDTO {
        private Long userId;
        private String nickname;
        private String profileImg;

        public UserWishlistDTO(User user) {
            this.userId = user.getId();
            this.nickname = user.getNickname();
            this.profileImg = user.getProfileImg();
        }
    }
}
