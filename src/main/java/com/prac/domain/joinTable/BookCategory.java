package com.prac.domain.joinTable;

import com.prac.domain.Author;
import com.prac.domain.Book;
import com.prac.domain.Category;
import com.prac.domain.etc.AuditingEntity;
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
public class BookCategory extends AuditingEntity {

    @EmbeddedId
    private BookCategoryKey id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", foreignKey = @ForeignKey(name = "fk_book_category_to_book"))
    @MapsId("bookId")
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", foreignKey = @ForeignKey(name = "fk_book_category_to_category"))
    @MapsId("categoryId")
    private Category category;


    public BookCategory(Book book, Category category) {
        this.book = book;
        this.category = category;
        this.id = new BookCategoryKey(book.getId(), category.getId());
    }
}
