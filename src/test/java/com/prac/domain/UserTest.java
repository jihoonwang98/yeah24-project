package com.prac.domain;

import com.prac.domain.etc.Address;
import com.prac.domain.etc.Gender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    Book book1 = Book.builder()
            .bio("book1입니다.")
            .isbn("384712938129")
            .name("book1")
            .price(30000)
            .publicationDate(LocalDate.now())
            .stockQuantity(100)
            .authors(new ArrayList<>())
            .build();

    Book book2 = Book.builder()
            .bio("book2입니다.")
            .isbn("43820932")
            .name("book2")
            .price(43000)
            .publicationDate(LocalDate.now())
            .stockQuantity(120)
            .authors(new ArrayList<>())
            .build();


    User user1 = User.builder()
            .address(null)
            .name("user1")
            .email("user1@naver.com")
            .gender(Gender.MALE)
            .username("user1id")
            .password("user2pwd")
            .nickname("asdf")
            .build();


    @Test
    @DisplayName("wishlist 저장 테스트")
    void test1() {
        assertThat(user1.getWishlist()).hasSize(0);

        // when
        user1.addToWishlist(book1, book2);

        // then
        assertThat(user1.getWishlist()).hasSize(2);
        assertThat(user1.getWishlist()).containsExactlyInAnyOrder(book1, book2);
    }

    @Test
    @DisplayName("wishlist 삭제 테스트")
    void test2() {
        // given
        assertThat(user1.getWishlist()).hasSize(0);
        user1.addToWishlist(book1, book2);

        // when
        user1.deleteInWishlist(book1);

        // then
        assertThat(user1.getWishlist()).hasSize(1);
        assertThat(user1.getWishlist()).containsExactlyInAnyOrder(book2);
    }
}