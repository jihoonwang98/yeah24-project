# [Yeah 24 프로젝트] 5. Author 클래스 작성   



## 1) Author 클래스 작성

앞에서 언급했듯이 `Book`과 `Author`는 다대다 관계다.

(도서 하나를 쓴 저자는 여러 명일 수 있다고 가정한다. 저자 한명이 여러 도서를 쓸 수 있는 것은 당연하다.)



**Author**

```java
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@AttributeOverride(name = "id", column = @Column(name = "author_id"))
public class Author extends BaseEntity {

    private String name;

    @Lob
    private String bio;

    private String imgSrc;

    public Author(String name, String bio) {
        this.name = name;
        this.bio = bio;
    }

    @Builder
    public Author(String name, String bio, String imgSrc) {
        this.name = name;
        this.bio = bio;
        this.imgSrc = imgSrc;
    }
}
```



book과 author 간의 다대다 매핑을 해주기 위해 `BookAuthor` 클래스를 작성해주자.



## 2) BookAuthor 클래스

**BookAuthorKey**

```java
@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Getter
public class BookAuthorKey implements Serializable {

    private Long bookId;

    private Long authorId;
}
```



**BookAuthor**

```java
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
```

- 앞서 작성했던 BookCategory 클래스와 원리가 똑같다.





## 3) Book 클래스

**Book**

```java
@Entity
...
public class Book extends BaseEntity {

    ...
        
    @OneToMany(mappedBy = "book", cascade = CascadeType.PERSIST)
    private List<BookAuthor> bookAuthors = new ArrayList<>();


    @Builder
    public Book(..., List<Author> authors) {
        ...
            
        for (Author author : authors) {
            this.bookAuthors.add(new BookAuthor(this, author));
        }
    }
}
```