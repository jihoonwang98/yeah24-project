package com.prac.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prac.domain.Book;
import com.prac.domain.User;
import com.prac.domain.joinTable.Review;
import com.prac.dto.UserDTO.UserReviewDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReviewDTO {

    @Data
    @AllArgsConstructor
    public static class ReviewBriefDTO {
        private Long bookId;
        private Long reviewCount;
        private Double avgRating;
    }


    @Data
    public static class ReviewDetailDTO {
        private Long bookId;
        private String name;
        private String content;
        private Double rating;
        private LocalDateTime regDate;

        private UserReviewDTO user;

        public ReviewDetailDTO(Review review) {
            this.bookId = review.getId().getBookId();
            this.name = review.getName();
            this.content = review.getContent();
            this.rating = review.getRating();
            this.regDate = review.getRegDate();
            this.user = new UserReviewDTO(review.getUser()); // 지연 로딩
        }
    }

    @Data
    public static class ReviewIntegrationDTO {
        private int count;
        private Double rating;
        private List<ReviewDetailDTO> reviews;

        public ReviewIntegrationDTO(List<Review> reviews) {
            if (reviews.size() != 0) {
                this.count = reviews.size();
                this.rating = reviews.stream().mapToDouble(r -> r.getRating()).average().getAsDouble();
                this.reviews = reviews.stream().map(r -> new ReviewDetailDTO(r)).collect(Collectors.toList());
            }
        }
    }

    @Data
    @NoArgsConstructor
    public static class ReviewPostDTO {

        private Long userId;
        private Long bookId;

        @NotBlank(message = "리뷰 제목을 입력해야 합니다.")
        private String name;

        @NotNull(message = "별점 평가를 해야 합니다.")
        private Double rating;

        @NotBlank(message = "리뷰 내용을 입력해야 합니다.")
        private String content;

        public Review toEntity(User user, Book book) {
            return Review.builder()
                    .user(user)
                    .book(book)
                    .name(name)
                    .content(content)
                    .rating(rating)
                    .build();
        }
    }


    @Data
    public static class ReviewWithoutUserDTO {
        private Long bookId;
        private String name;
        private String content;
        private Double rating;
        private LocalDateTime regDate;

        public ReviewWithoutUserDTO(Review review) {
            this.bookId = review.getId().getBookId();
            this.name = review.getName();
            this.content = review.getContent();
            this.rating = review.getRating();
            this.regDate = review.getRegDate();
        }
    }



    @Data
    public static class ReviewByUserDTO {
        private UserReviewDTO user;
        private List<ReviewWithoutUserDTO> reviews;
        private Double avgRating;
        private Integer count;

        public ReviewByUserDTO(User user, List<Review> reviews) {
            this.user = new UserReviewDTO(user);
            this.reviews = reviews.stream().map(r -> new ReviewWithoutUserDTO(r)).collect(Collectors.toList());
            this.avgRating = reviews.stream().mapToDouble(r -> r.getRating()).average().getAsDouble();
            this.count = reviews.size();
        }

        public ReviewByUserDTO(User user) {
            this.user = new UserReviewDTO(user);
            this.avgRating = 0.0;
            this.count = 0;
        }
    }

    @Data
    public static class ReviewDeleteDTO {
        private Long userId;
        private Long bookId;
    }
}
