# [Yeah 24 프로젝트] 4. Book 클래스 작성



## 1) Book 클래스 작성

**Book**

```java
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


    // Author 클래스 작성할 때 설명한다.
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
```

- `Book`과 `Category`의 관계가 <u>**다대다 관계**</u>이므로 중간에 Join Table 클래스 `BookCategory`를 만들어주고, `@OneToMany` 애너테이션으로 매핑해줬다.
- 마찬가지로, `Book`과 `Author`의 관계 역시 **<u>다대다 관계</u>**이므로 Join Table 클래스 `BookAuthor`를 두고, `@OneToMany`로 매핑해준다.



- 필드 설명

  - name: 책 이름

  - bio: 책 설명

  - publicationDate: 출판일

  - price: 책 정가

  - stockQuantity: 책 재고 수량

  - isbn: 책의 isbn

  - imgSrc: 책의 이미지 file이 저장된 path

  - bookCategories

    - 현재 Book에 해당하는 BookCategory들을 담고 있는 리스트

      - 어떤 Book의 Category 분류가 **국내도서 > 소설/시/희곡 > 한국소설 > 한국 단편소설**에 속한다고 하자.

        이 경우에는 이 Book의 bookCategories 리스트에는 **국내도서, 소설/시/희곡, 한국소설, 한국 단편 소설** 이 네가지에 해당하는 BookCategory 인스턴스 4개가 들어가게 된다.

    - cascade 전략을 `PERSIST`로 설정해서 bookCategories 리스트에 BookCategory를 담아서 저장하면 Book을 저장할 때 BookCategory들도 함께 저장될 수 있게 했다.

  - bookAuthors

    - bookCategories와 같은 원리다. 다만, 저장하는 클래스가 `BookAuthor`이다.





- `Book`을 생성할 때는 전달받을 인자가 많기 때문에 빌더 패턴을 사용했다.
  - 주의할 점은 `Category highestDepthCategory`라는 파라미터에 해당 도서의 가장 깊이가 깊은 카테고리를 넘겨줘야 한다는 점이다.
  - 예를 들면, 어떤 도서의 카테고리 분류가 **국내도서 > 소설/시/희곡 > 한국소설 > 한국 단편소설**에 속한다고 하자. 그러면 `highestDepthCategory`로 **한국 단편소설**을 넘겨줘야 한다.



- List\<Category> getAllParents(Category highestDepthCategory) 메서드

  - 카테고리가 **국내도서 > 소설/시/희곡 > 한국소설 > 한국 단편소설**와 같이 분류되어 있다고 하자.

  - highestDepthCategory로 "한국 단편소설" 카테고리가 들어오면 

    [국내도서, 소설/시/희곡, 한국소설, 한국 단편소설] 카테고리 리스트를 리턴한다.



- void minusStockQuantity(int stockQuantity)
  - 파라미터로 넘겨받은 stockQuantity만큼 Book의 재고 수량을 줄인다.



- void plusStockQuantity(int stockQuantity) 
  - 파라미터로 넘겨받은 stockQuantity만큼 Book의 재고 수량을 늘린다. (주문 취소시에 다시 재고 수량을 늘릴 때 사용)





## 2) BookCategory 클래스 작성

**BookCategoryKey**

```java
@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Getter
public class BookCategoryKey implements Serializable {

    private Long bookId;

    private Long categoryId;
}
```

- 식별자 클래스의 조건
  - `Serializable` 인터페이스 구현
  - `equals()`, `hashCode()` 구현
  - 기본 생성자 선언
  - 클래스의 접근자가 public
  - 식별자 클래스에 `@Embeddable` 붙여야 함.

- Book의 키와 Category 키를 합쳐서 복합키로 만들어준다.





**BookCategory**

```java
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
```

- 위에서 만들어준 복합키를 필드로 선언하고 `@EmbeddedId` 애너테이션을 붙여준다.
- `BookCategory` 입장에서 Book과 Category 모두 **<u>다대일 관계</u>**이므로 `@ManyToOne` 애너테이션을 붙여준다.









## 3) Book 클래스 테스트

```java
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
}
```

- `Book` 생성시에 `Category`들이 잘 들어가는지 테스트한다.





## 4) **DB Schema**

![schema](https://lh5.googleusercontent.com/cfaRi8a3h3g0OFpud9UDNypdjbxcnBZWXtPPBN2ryugZwOJEYePFyYNO3_GMgNo5O_4Cnvbpbr61o25b5Bc0BfKsiRLUP3TrWg2vtJJk21mlkqvkB5jyzOJcBARwl0xx3d22R-_S)







