package com.prac.domain.joinTable;

import com.prac.domain.Author;
import com.prac.domain.Book;
import com.prac.domain.Category;
import com.prac.domain.etc.AuditingEntity;
import com.prac.domain.etc.BookAuthorKey;
import com.prac.domain.etc.BookCategoryKey;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookAuthor extends AuditingEntity {

    @EmbeddedId
    private BookAuthorKey id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", foreignKey = @ForeignKey(name = "fk_book_author_to_book"))
    @MapsId("bookId")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", foreignKey = @ForeignKey(name = "fk_book_author_to_author"))
    @MapsId("authorId")
    private Author author;


    public BookAuthor(Book book, Author author) {
        this.book = book;
        this.author = author;
        this.id = new BookAuthorKey(book.getId(), author.getId());
    }
}
