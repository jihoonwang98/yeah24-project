# [Yeah 24 프로젝트] 6. User 클래스, Wishlist 조인 테이블, Review 조인 테이블 구현하기



## 1) User 클래스 작성

![schema](https://lh6.googleusercontent.com/4kc35oe9FuGGgx7UezeaRB90cOFG_J_44_Ti8yRFkH3nbfVyXHUkVsbwjdT8kPggZ68CdCPU37n4878y8Ob4iVVTfuQRNASYc7v30LUp2zsR_ZIJQ5D-p-RnVfvg72oam_l-S-hq)



이번 포스팅은 User 클래스, Wishlist 조인 테이블, 그리고 Review 조인 테이블을 구현해보겠다.





**User**

```java
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@AttributeOverride(name = "id", column = @Column(name = "user_id"))
public class User extends BaseEntity {

    private String username;

    private String password;

    private String nickname;

    private String name;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Embedded
    private Address address;

    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String provider;

    private String providerId;

    private String profileImg;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "wish_list",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id", foreignKey = @ForeignKey(name = "fk_wishlist_to_user")),
            inverseJoinColumns = @JoinColumn(name = "book_id", referencedColumnName = "book_id", foreignKey = @ForeignKey(name = "fk_wishlist_to_book"))
    )
    private Set<Book> wishlist = new HashSet<>();


    @Builder
    public User(String username, String password, String nickname, String name, Gender gender, Address address, String email, Role role, String provider, String providerId, String profileImg) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.name = name;
        this.gender = gender;
        this.address = address;
        this.email = email;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
        this.profileImg = profileImg;
    }

    public void addToWishlist(Book... books) {
        this.wishlist.addAll(Arrays.asList(books));
    }

    public void addToWishlist(List<Book> books) {
        this.wishlist.addAll(books);
    }

    public void deleteInWishlist(Book book) {
        this.wishlist.remove(book);
    }

    public void modify(String password, String nickname, String profileImg, Address address) {
        this.password = password;
        this.nickname = nickname;

        if (profileImg != null) {
            this.profileImg = profileImg;
        }

        this.address = new Address(address.getPostcode(), address.getRoadAddr(), address.getJibunAddr(), address.getDetailAddr(), address.getExtraAddr());
    }

}
```

- 필드 설명
  - username: 아이디
  - password: User의 패스워드
  - nickname: 닉네임
  - name: 이름
  - gender: MALE(남자) 또는 FEMALE(여자)
  - address: 주소 (임베디드 값 타입)
  - email: 이메일
  - role: ROLE_USER(일반 유저) 또는 ROLE_ADMIN(어드민)
  - provider: basic-join(일반 회원가입) 또는 google(구글 회원가입) 또는 naver(네이버 회원가입)
  - providerId: OAuth2 회원가입 시에 제공받는 unique한 id
  - profileImg: User의 프로필 이미지 file이 저장되어 있는 path
  - wishlist: wishlist를 저장하는 Set



- void addToWishlist(Book... books)와 void addToWishlist(List\<Book> books)
  - wishlist에 파라미터로 넘겨받은 Book들을 추가한다.
- void deleteInWishlist(Book book)
  - wishlist에서 파라미터로 넘겨받은 Book을 삭제한다.
- void modify(String password, String nickname, String profileImg, Address address) 
  - 회원 정보 수정을 위해 존재하는 메서드
  - 넘겨받은 password, nickname, profileImg, address로 회원 정보를 수정한다.





## 2) Wishlist 조인 테이블 구현

**Wishlist 테이블 추가**

```java
@Entity
...
public class User extends BaseEntity {

    ...

    @ManyToMany
    @JoinTable(
            name = "wish_list",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id", referencedColumnName = "book_id")
    )
    private Set<Book> wishlist = new HashSet<>();
}
```

- wishlist의 경우에는 중간 테이블(Join Table)에 저장할 컬럼이 없기 때문에 편리하게 매핑하기 위해서 `@ManyToMany` 애너테이션을 사용해 매핑해줬다.
- `@ManyToMany` 애너테이션을 사용할 때 주의할 점은 `List`를 사용해 매핑하는 경우 성능 이슈가 발생할 수 있다는 점이다. 따라서 `Set`을 이용해 매핑하는 것이 좋다.
  - 참고: https://dzone.com/articles/why-set-is-better-than-list-in-manytomany









## 3) Review 조인 테이블 구현

한 명의 User가 여러 개의 Book에 대해 Review를 작성할 수 있고,

여러 명의 User가 한 개의 Book에 대해 Review를 작성할 수 있으므로

User와 Book의 Review 관계는 다대다 매핑으로 해결해야 한다.



위에서의 다대다 매핑 방법(@ManyToMany)은 한가지 결점이 있다.

바로 **<u>테이블에 추가 컬럼을 넣을 수 없다</u>**는 점이다.

하지만, Review 테이블은 별점, 리뷰 내용 등 추가 컬럼을 넣어줘야 하므로 직접 Join Table 엔티티를 작성해주자.



**ReviewKey**

```java
@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Getter
public class ReviewKey implements Serializable {

    private Long userId;

    private Long bookId;
}

```

- Review가 user_id, book_id 두 개를 복합키로 사용하고 있기 때문에 복합키 클래스를 정의해준다.



**Review**

```java
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
```

- Review 입장에서 Review 대 User는 다대일 관계, Review 대 Book 역시 다대일 관계이므로 **@ManyToOne** 애너테이션으로 연관관계를 설정해준다.

