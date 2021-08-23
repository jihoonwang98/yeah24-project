package com.prac.domain;

import com.prac.domain.etc.BaseEntity;
import com.prac.domain.etc.BookStatus;
import com.prac.domain.joinTable.BookAuthor;
import com.prac.domain.joinTable.BookCategory;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(exclude = {"bookCategories", "bookAuthors", "bio"})
@AttributeOverride(name = "id", column = @Column(name = "book_id"))
public class Book extends BaseEntity {

    private String name;

    @Lob
    private String bio;

    private LocalDate publicationDate;

    private int price;

    private int stockQuantity;

    private String isbn;

    private String imgSrc;


    @OneToMany(mappedBy = "book", cascade = CascadeType.PERSIST)
    private List<BookCategory> bookCategories = new ArrayList<>();


    @OneToMany(mappedBy = "book", cascade = CascadeType.PERSIST)
    private List<BookAuthor> bookAuthors = new ArrayList<>();


    @Builder
    public Book(String name, String bio, LocalDate publicationDate, int price,
                int stockQuantity, String isbn, String imgSrc, Category highestDepthCategory, List<Author> authors) {
        this.name = name;
        this.bio = bio;
        this.publicationDate = publicationDate;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.isbn = isbn;
        this.imgSrc = imgSrc;

        List<Category> categories = getAllParents(highestDepthCategory);
        for (Category category : categories) {
            this.bookCategories.add(new BookCategory(this, category));
        }

        for (Author author : authors) {
            this.bookAuthors.add(new BookAuthor(this, author));
        }
    }


    private List<Category> getAllParents(Category highestDepthCategory) {
        List<Category> results = new ArrayList<>();

        Category iter = highestDepthCategory;
        while (iter != null) {
            results.add(iter);
            iter = iter.getParent();
        }

        return results;
    }


    public void minusStockQuantity(int stockQuantity) {
        this.stockQuantity -= stockQuantity;

        if(this.stockQuantity < 0) {
            this.stockQuantity = 0;
        }
    }

    public void plusStockQuantity(int stockQuantity) {
        this.stockQuantity += stockQuantity;
    }
}
