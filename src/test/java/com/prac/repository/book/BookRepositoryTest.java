package com.prac.repository.book;

import com.prac.config.BasicConfiguration;
import com.prac.domain.Author;
import com.prac.domain.Book;
import com.prac.domain.Category;
import com.prac.domain.User;
import com.prac.domain.joinTable.Review;
import com.prac.dto.*;
import com.prac.dto.AuthorDTO.AuthorBriefDTO;
import com.prac.dto.BookDTO.*;
import com.prac.dto.CategoryDTO.CategoryBriefDTO;
import com.prac.dto.CategoryDTO.CategoryBriefDTOWithChildren;
import com.prac.dto.ReviewDTO.ReviewDetailDTO;
import com.prac.dto.ReviewDTO.ReviewIntegrationDTO;
import com.prac.dto.UserDTO.UserWishlistDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(BasicConfiguration.class)
class BookRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    BookRepository bookRepository;

    Category parent;
    Category child1;
    Category child2;
    Category grandChild;

    List<User> users;
    User user1;
    User user2;
    User user3;
    User user4;

    Review review1;
    Review review2;
    Review review3;
    Review review4;

    Book book1;
    Author book1Author1;
    Author book1Author2;
    Author book1Author3;

    Book book2;
    Author book2Author1;
    Author book2Author2;

    Book book3;
    Author book3Author1;

    @BeforeEach
    void setUp() {
        parent = new Category("parent", null);
        child1 = new Category("child1", parent);
        child2 = new Category("child2", parent);
        grandChild = new Category("grandchild", child1);

        entityManager.persist(parent);

        users = new ArrayList<>();

        user1 = User.builder()
                .username("username1")
                .name("name1")
                .build();
        users.add(user1);
        entityManager.persist(user1);

        user2 = User.builder()
                .username("username2")
                .name("name2")
                .build();
        users.add(user2);
        entityManager.persist(user2);

        user3 = User.builder()
                .username("username3")
                .name("name3")
                .build();
        users.add(user3);
        entityManager.persist(user3);

        user4 = User.builder()
                .username("username4")
                .name("name4")
                .build();
        users.add(user4);
        entityManager.persist(user4);


        book1Author1 = Author.builder().name("book1's author1").build();
        book1Author2 = Author.builder().name("book1's author2").build();
        book1Author3 = Author.builder().name("book1's author3").build();

        book1 = Book.builder()
                .name("book1")
                .bio("book1의 bio입니다. 아주 좋은 책입니다.")
                .highestDepthCategory(grandChild)
                .authors(new ArrayList<Author>() {
                    {
                        add(book1Author1);
                        add(book1Author2);
                        add(book1Author3);
                    }
                })
                .build();

        entityManager.persist(book1Author1);
        entityManager.persist(book1Author2);
        entityManager.persist(book1Author3);
        entityManager.persist(book1);



        book2Author1 = Author.builder().name("book2's author1").build();
        book2Author2 = Author.builder().name("book2's author2").build();

        book2 = Book.builder()
                .name("book2")
                .highestDepthCategory(child1)
                .bio("book2의 bio입니다. 신용카드 전표/고객용 이용해 주셔서 감사합니다.")
                .authors(new ArrayList<Author>() {
                    {
                        add(book2Author1);
                        add(book2Author2);
                    }
                })
                .build();

        entityManager.persist(book2Author1);
        entityManager.persist(book2Author2);
        entityManager.persist(book2);



        book3Author1 = Author.builder().name("book3's author1").build();
        book3 = Book.builder()
                .name("book3")
                .highestDepthCategory(child2)
                .bio("book3의 소개입니다. Supplies")
                .authors(new ArrayList<Author>() {
                    {
                        add(book3Author1);
                    }
                })
                .build();

        entityManager.persist(book3Author1);
        entityManager.persist(book3);


        review1 = Review.builder()
                .name("book1의 리뷰 by user1")
                .user(user1)
                .book(book1)
                .rating(3.5)
                .content("book1의 리뷰 by user1입니다.")
                .build();

        review2 = Review.builder()
                .name("book1의 리뷰 by user2")
                .user(user2)
                .book(book1)
                .rating(5.0)
                .content("book1의 리뷰 by user2입니다.")
                .build();

        review3 = Review.builder()
                .name("book1의 리뷰 by user3")
                .user(user3)
                .book(book1)
                .rating(1.5)
                .content("book1의 리뷰 by user3입니다.")
                .build();

        review4 = Review.builder()
                .name("book1의 리뷰 by user4")
                .user(user4)
                .book(book1)
                .rating(2.5)
                .content("book1의 리뷰 by user4입니다.")
                .build();

        entityManager.persist(review1);
        entityManager.persist(review2);
        entityManager.persist(review3);
        entityManager.persist(review4);


        user1.addToWishlist(book1, book3);

        entityManager.flush();
    }

    @Test
    void findBooksByCategory() {
        BookListResponse response = bookRepository.findBooksByCategory(parent.getId(), PageRequest.of(0, 10), null);

        CategoryBriefDTOWithChildren category = response.getCategory();
        assertThat(category.getId()).isEqualTo(parent.getId());
        assertThat(category.getName()).isEqualTo(parent.getName());
        assertThat(category.getDepth()).isEqualTo(parent.getDepth());

        List<CategoryBriefDTO> categoryChildren = category.getChildren();
        assertThat(categoryChildren).hasSize(2);
        assertThat(categoryChildren).containsExactlyInAnyOrder(new CategoryBriefDTO(child1), new CategoryBriefDTO(child2));


        List<BookListDTO> books = response.getBooks();
        assertThat(books).hasSize(3);

        BookListDTO book1 = books.stream().filter(b -> b.getName().equals("book1")).findFirst().get();
        BookListDTO book2 = books.stream().filter(b -> b.getName().equals("book2")).findFirst().get();
        BookListDTO book3 = books.stream().filter(b -> b.getName().equals("book3")).findFirst().get();

        assertThat(book1.getReview().getReviewCount()).isEqualTo(4);
        assertThat(book1.getReview().getAvgRating()).isEqualTo((3.5 + 5.0 + 1.5 + 2.5) / 4);

        assertThat(book2.getReview()).isNull();
        assertThat(book3.getReview()).isNull();


        assertThat(book1.getAuthors()).hasSize(3);
        assertThat(book1.getAuthors()).extracting("name").containsExactlyInAnyOrder("book1's author1", "book1's author2", "book1's author3");

        assertThat(book2.getAuthors()).hasSize(2);
        assertThat(book2.getAuthors()).extracting("name").containsExactlyInAnyOrder("book2's author1", "book2's author2");

        assertThat(book3.getAuthors()).hasSize(1);
        assertThat(book3.getAuthors()).extracting("name").containsExactlyInAnyOrder("book3's author1");
    }

    @Test
    void findBookDetailById() {
        BookDetailResponse response = bookRepository.findBookDetailById(book1.getId());

        BookDetailDTO book = response.getBook();
        assertThat(book.getId()).isEqualTo(book1.getId());
        assertThat(book.getName()).isEqualTo(book1.getName());

        ReviewIntegrationDTO review = book.getReview();
        assertThat(review.getCount()).isEqualTo(4);
        assertThat(review.getRating()).isEqualTo((3.5 + 5.0 + 1.5 + 2.5) / 4);

        List<ReviewDetailDTO> reviews = review.getReviews();
        assertThat(reviews).hasSize(4);
        assertThat(reviews).containsExactlyInAnyOrder(new ReviewDetailDTO(review1), new ReviewDetailDTO(review2), new ReviewDetailDTO(review3), new ReviewDetailDTO(review4));

        List<CategoryBriefDTO> categories = response.getCategories();
        assertThat(categories).hasSize(3);
        assertThat(categories).containsExactlyInAnyOrder(new CategoryBriefDTO(parent), new CategoryBriefDTO(child1), new CategoryBriefDTO(grandChild));

        List<AuthorBriefDTO> authors = response.getAuthors();
        assertThat(authors).hasSize(3);
        assertThat(authors).containsExactlyInAnyOrder(new AuthorBriefDTO(book1Author1), new AuthorBriefDTO(book1Author2), new AuthorBriefDTO(book1Author3));
    }

    @Test
    void findBookWishlistResponse() {
        BookWishlistResponse response = bookRepository.findBookWishlistResponse(user1.getId());

        UserWishlistDTO user = response.getUser();
        assertThat(user.getUserId()).isEqualTo(user1.getId());

        List<BookWishlistDTO> books = response.getBooks();
        assertThat(books).containsExactlyInAnyOrder(new BookWishlistDTO(book1), new BookWishlistDTO(book3));
        assertThat(books).hasSize(2);
    }

    @Test
    void findNewBooks() {
        Book b1 = Book.builder()
                .name("b1")
                .publicationDate(LocalDate.of(2019, 10, 8))
                .authors(new ArrayList<Author>() {
                    {
                        add(book1Author1);
                    }
                })
                .build();

        Book b2 = Book.builder()
                .name("b2")
                .publicationDate(LocalDate.of(1992, 4, 12))
                .authors(new ArrayList<Author>() {
                    {
                        add(book1Author1);
                    }
                })
                .build();

        Book b3 = Book.builder()
                .name("b3")
                .publicationDate(LocalDate.of(2021, 12, 25))
                .authors(new ArrayList<Author>() {
                    {
                        add(book1Author1);
                    }
                })
                .build();

        Book b4 = Book.builder()
                .name("b4")
                .publicationDate(LocalDate.of(2020, 8, 5))
                .authors(new ArrayList<Author>() {
                    {
                        add(book1Author1);
                    }
                })
                .build();

        Book b5 = Book.builder()
                .name("b5")
                .publicationDate(LocalDate.of(2018, 9, 6))
                .authors(new ArrayList<Author>() {
                    {
                        add(book1Author1);
                    }
                })
                .build();

        Book b6 = Book.builder()
                .name("b6")
                .publicationDate(LocalDate.of(1998, 4, 7))
                .authors(new ArrayList<Author>() {
                    {
                        add(book1Author1);
                    }
                })
                .build();

        entityManager.persist(b1);
        entityManager.persist(b2);
        entityManager.persist(b3);
        entityManager.persist(b4);
        entityManager.persist(b5);
        entityManager.persist(b6);

        List<BookListDTO> newBooks = bookRepository.findNewBooks(3L);
        assertThat(newBooks).hasSize(3);
        assertThat(newBooks).extracting("publicationDate").containsExactlyInAnyOrder(
                LocalDate.of(2021, 12, 25),
                LocalDate.of(2020, 8, 5),
                LocalDate.of(2019, 10, 8)
        );
    }

    @Test
    void findBooksInCart() {

        Long b1Id = book1.getId();
        Long b2Id = book2.getId();
        Long b3Id = book3.getId();

        List<BookCartDTO> booksInCart = bookRepository.findBooksInCart(Arrays.asList(b1Id, b2Id, b3Id));
        assertThat(booksInCart).hasSize(3);
        assertThat(booksInCart).containsExactlyInAnyOrder(new BookCartDTO(book1), new BookCartDTO(book2), new BookCartDTO(book3));

    }

    @Test
    void findBookSearchResponse() {

        Book b1 = Book.builder()
                .name("dog")
                .publicationDate(LocalDate.of(2019, 10, 8))
                .bio("뭐라고 쓸지 고민되는 군요..")
                .authors(new ArrayList<Author>() {
                    {
                        add(book1Author1);
                    }
                })
                .build();

        Book b2 = Book.builder()
                .name("cat")
                .publicationDate(LocalDate.of(1992, 4, 12))
                .bio("테스트 케이스를 만드는 것이 항상 귀찮습니다.")
                .authors(new ArrayList<Author>() {
                    {
                        add(book1Author1);
                    }
                })
                .build();

        Book b3 = Book.builder()
                .name("swim")
                .publicationDate(LocalDate.of(2021, 12, 25))
                .bio("하지만 테스트 케이스를 작성하는 것은 중요합니다.")
                .authors(new ArrayList<Author>() {
                    {
                        add(book1Author1);
                    }
                })
                .build();

        Book b4 = Book.builder()
                .name("balloon")
                .publicationDate(LocalDate.of(2020, 8, 5))
                .bio("귀찮아도 참고 작성해야합니다.")
                .authors(new ArrayList<Author>() {
                    {
                        add(book1Author1);
                    }
                })
                .build();

        Book b5 = Book.builder()
                .name("bottle")
                .publicationDate(LocalDate.of(2018, 9, 6))
                .bio("이제 진짜 할 말이 떨어졌습니다.")
                .authors(new ArrayList<Author>() {
                    {
                        add(book1Author1);
                    }
                })
                .build();

        Book b6 = Book.builder()
                .name("smartphone")
                .publicationDate(LocalDate.of(1998, 4, 7))
                .bio("뭐라고 쓰면 좋을까요")
                .authors(new ArrayList<Author>() {
                    {
                        add(book1Author1);
                    }
                })
                .build();

        entityManager.persist(b1);
        entityManager.persist(b2);
        entityManager.persist(b3);
        entityManager.persist(b4);
        entityManager.persist(b5);
        entityManager.persist(b6);

        BookSearchResponse bookSearchResponse = bookRepository.findBookSearchResponse("name", "book", PageRequest.of(0, 10), null);

        PageResultDTO<BookListDTO> pageResult = bookSearchResponse.getPageResult();
        System.out.println(pageResult);
        List<BookListDTO> books = bookSearchResponse.getBooks();
        System.out.println(books);
    }
}