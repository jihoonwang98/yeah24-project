package com.prac.repository.author;

import com.prac.config.BasicConfiguration;
import com.prac.domain.Author;
import com.prac.domain.Book;
import com.prac.domain.joinTable.BookAuthor;
import com.prac.dto.AuthorDTO;
import com.prac.dto.AuthorDTO.AuthorDetailDTO;
import com.prac.dto.BookDTO;
import com.prac.dto.BookDTO.BookAuthorDTO;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.prac.domain.QAuthor.author;
import static com.prac.domain.QBook.book;
import static com.prac.domain.joinTable.QBookAuthor.bookAuthor;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(BasicConfiguration.class)
class AuthorRepositoryTest {

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    AuthorRepository authorRepository;

    Author author;

    List<Book> bookList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        author = Author.builder()
                .name("author")
                .build();
        testEntityManager.persist(author);

        for (int i = 1; i <= 3; i++) {
            Book book = Book.builder()
                    .name("book" + i)
                    .authors(new ArrayList<Author>() {
                        {
                            add(author);
                        }
                    })
                    .build();
            bookList.add(book);

            testEntityManager.persist(book);
        }
    }

    @Test
    void test1() {
        AuthorDetailDTO authorDetail = authorRepository.findAuthorDetailById(author.getId());

        assertThat(authorDetail.getId()).isEqualTo(author.getId());
        assertThat(authorDetail.getName()).isEqualTo(author.getName());

        assertThat(authorDetail.getBooks()).hasSize(bookList.size());
        assertThat(authorDetail.getBooks()).extracting("name").containsExactlyInAnyOrder("book1", "book2", "book3");

    }
}