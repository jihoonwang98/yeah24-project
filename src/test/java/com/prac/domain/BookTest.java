package com.prac.domain;

import com.prac.domain.joinTable.BookAuthor;
import com.prac.domain.joinTable.BookCategory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class BookTest {

    Category parent = new Category("parent", null);
    Category child1 = new Category("child1", parent);
    Category child2 = new Category("child2", parent);
    Category grandChild1 = new Category("grandchild1", child1);
    Category grandChild2 = new Category("grandchild2", child2);

    Author author1 = new Author("author1", "author1입니다.");
    Author author2 = new Author("author2", "author2입니다.");

    List<Author> authors = new ArrayList<Author>() {
        {
            add(author1);
            add(author2);
        }
    };


    Book book1 = Book.builder()
            .bio("book1입니다.")
            .isbn("384712938129")
            .name("book1")
            .highestDepthCategory(grandChild1)
            .price(30000)
            .publicationDate(LocalDate.now())
            .stockQuantity(100)
            .authors(authors)
            .build();

    Book book2 = Book.builder()
            .bio("book2입니다.")
            .isbn("43820932")
            .name("book2")
            .highestDepthCategory(child2)
            .price(43000)
            .publicationDate(LocalDate.now())
            .stockQuantity(120)
            .authors(authors)
            .build();


    @Test
    @DisplayName("Book, Category 연관관계 테스트")
    void testAssociation() {
        List<BookCategory> bookCategories = book1.getBookCategories();
        List<Category> categories = bookCategories.stream().map(bc -> bc.getCategory()).collect(Collectors.toList());

        assertThat(bookCategories).hasSize(3);
        assertThat(bookCategories).allMatch(bookCategory -> bookCategory.getBook().equals(book1));
        assertThat(categories).containsExactlyInAnyOrder(grandChild1, child1, parent);
    }


    @Test
    @DisplayName("Book, Authors 연관관계 테스트")
    void testAssociation2() {
        List<BookAuthor> bookAuthors = book1.getBookAuthors();
        List<Author> authors = bookAuthors.stream().map(ba -> ba.getAuthor()).collect(Collectors.toList());

        assertThat(bookAuthors).hasSize(2);
        assertThat(bookAuthors).allMatch(bookAuthor -> bookAuthor.getBook().equals(book1));
        assertThat(authors).containsExactlyInAnyOrder(author1, author2);
    }
}