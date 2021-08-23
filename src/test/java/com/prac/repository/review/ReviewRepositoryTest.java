package com.prac.repository.review;

import com.prac.config.BasicConfiguration;
import com.prac.domain.Author;
import com.prac.domain.Book;
import com.prac.domain.User;
import com.prac.domain.joinTable.Review;
import com.prac.dto.ReviewDTO;
import com.prac.dto.ReviewDTO.ReviewByUserDTO;
import com.prac.dto.ReviewDTO.ReviewIntegrationDTO;
import com.prac.dto.ReviewDTO.ReviewWithoutUserDTO;
import com.prac.dto.UserDTO;
import com.prac.dto.UserDTO.UserReviewDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(BasicConfiguration.class)
class ReviewRepositoryTest {

    @Autowired
    TestEntityManager em;

    @Autowired
    ReviewRepository reviewRepository;

    User user;

    Author author1;
    Book book1;

    Author author2;
    Book book2;

    Review review1;
    Review review2;


    @BeforeEach
    void setUp() {
        user = User.builder().name("user").username("username").build();
        author1 = new Author("author", "author입니다.");
        book1 = Book.builder()
                .bio("book1입니다.")
                .name("book1")
                .authors(new ArrayList<Author>() {
                    {
                        add(author1);
                    }
                })
                .build();
        author2 = new Author("author2", "author2입니다.");
        book2 = Book.builder()
                .bio("book2입니다.")
                .name("book2")
                .authors(new ArrayList<Author>(){
                    {
                        add(author2);
                    }
                })
                .build();
        review1 = Review.builder()
                .name("review1")
                .book(book1)
                .user(user)
                .content("review1입니다.")
                .rating(3.5)
                .build();

        review2 = Review.builder()
                .name("review2")
                .book(book2)
                .user(user)
                .content("review2입니다.")
                .rating(5.0)
                .build();


        em.persist(author1);
        em.persist(author2);
        em.persist(book1);
        em.persist(book2);
        em.persist(review1);
        em.persist(review2);
    }

    @Test
    void findReviewsByUser() {

        ReviewByUserDTO reviewsByUser = reviewRepository.findReviewsByUser(user.getId());
        assertThat(reviewsByUser.getAvgRating()).isEqualTo((3.5 + 5.0) / 2);
        assertThat(reviewsByUser.getCount()).isEqualTo(2);
        assertThat(reviewsByUser.getUser()).isEqualTo(new UserReviewDTO(user));

        List<ReviewWithoutUserDTO> reviews = reviewsByUser.getReviews();
        ReviewWithoutUserDTO r1 = new ReviewWithoutUserDTO(review1);
        ReviewWithoutUserDTO r2 = new ReviewWithoutUserDTO(review2);

        assertThat(reviews).hasSameElementsAs(Arrays.asList(r1, r2));
    }

    @Test
    void findReviewsByUserWhenNoReview() {

    }
}