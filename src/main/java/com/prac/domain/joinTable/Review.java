package com.prac.domain.joinTable;

import com.prac.domain.Book;
import com.prac.domain.User;
import com.prac.domain.etc.AuditingEntity;
import com.prac.domain.etc.BaseEntity;
import com.prac.domain.etc.ReviewKey;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@ToString(of = {"name", "rating", "content"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends AuditingEntity {

    @EmbeddedId
    private ReviewKey id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_review_to_user"))
    @MapsId("userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", foreignKey = @ForeignKey(name = "fk_review_to_book"))
    @MapsId("bookId")
    private Book book;


    private String name;

    private Double rating;

    @Lob
    private String content;

    @Builder
    public Review(User user, Book book, String name, Double rating, String content) {
        this.user = user;
        this.book = book;
        this.id = new ReviewKey(user.getId(), book.getId());
        this.name = name;
        this.rating = rating;
        this.content = content;
    }
}
