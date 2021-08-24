# [Yeah 24 프로젝트] 8. Service, Repository 계층 구현하기



## 1. Repository (QueryDsl)



### 왜 QueryDsl을 사용하는가?

- SQL, JPQL은 문자열이기 때문에 컴파일 시에 오류를 잡기가 힘들다.
- QueryDsl은 자바 코드로 작성하기 때문에 IDE의 도움을 받아 문법 오류를 잡기가 좋다.
- QueryDsl은 동적 쿼리 작성이 편리하다.





### 어떻게 QueryDsl을 사용하는가?

우리 프로젝트에서는 쿼리를 작성할 때 QueryDsl를 사용하므로, Spring Data JPA와 같이 사용하려면 아래와 같은 구조로 클래스를 작성해주어야 한다.



![](https://oopy.lazyrockets.com/api/v2/notion/image?src=https%3A%2F%2Fs3-us-west-2.amazonaws.com%2Fsecure.notion-static.com%2Fc7e1915a-6ea2-40ac-8009-2534346858d2%2F_2020-07-16__9.19.25.png&blockId=5f3a5408-fc28-415b-a22f-ba5335644ccd)



- Spring Data JPA와 함께 Querydsl를 사용하고 싶다고 하자.

- 만약, 위와 같이 Member 엔티티를 관리하는 `MemberRepository` 인터페이스를 `JpaRepository`만 상속해주면 Spring Data JPA의 기능을 이용할 수 있다. 

- 이 상태에서 QueryDsl을 사용하고 싶다면 `MemberRepository` 인터페이스를 `MemberRepositoryCustom` 이라는 인터페이스를 상속하게 만들어주고, 

  `MemberRepositoryCustom`이라는 인터페이스를 구현하는 `MemberRepositoryImpl`이라는 구체 클래스를 작성해주면 된다.

  - `MemberRepositoryCustom`에는 작성하고 싶은 메서드 시그니처를 정의해 놓고, `MemberRepositoryImpl`이라는 구체 클래스에서 QueryDsl을 이용해 직접 쿼리를 작성하면 된다.





### 예시

**UserRepositoryImpl**

```java
@Transactional
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    @Transactional(readOnly = true)
    public UserDetailDTO findUserDetailDTO(Long id) {
        return queryFactory
            	.select(Projections.constructor(UserDetailDTO.class, user))
                .from(user)
                .where(user.id.eq(id))
                .fetchOne();
    }

}
```

- `JPAQueryFactory`를 Bean으로 등록해 놓고 주입받아서 사용한다.







## 2. Service



### AuthorService

- `AuthorDetailDTO findAuthorDetailById(Long id)`
  - Author의 id 값을 받아서 해당 Author의 정보를 AuthorDetailDTO 형태로 반환한다.



### BookService

- `BookListResponse findBooksByCategoryId(Long id, Pageable pageable, String orderBy)`

  - 카테고리 id, Pageable 객체, 정렬 기준 문자열을 받아서 

    해당 카테고리에 속하는 도서들을 정렬한 후 페이징해서 반환한다.



- `BookDetailResponse findBookDetailByBookId(Long id)`
  - Book의 id 값을 받아서 Book의 상세 정보를 BookDetailResponse 형태로 반환한다.



- `List<BookListDTO> findNewBooks()`
  - 홈페이지에서 신간 도서를 출력할 때 사용되는 메서드다.
  - 출판일 기준으로 가장 최신 도서 5권을 List\<BookListDTO> 형태로 반환한다.



- `List<BookCartDTO> findBooksInCart(List<Long> bookIds)`

  - 장바구니에 들어있는 Book들의 id들을 파라미터로 받아서

    Book들의 정보를 List\<BookCartDTO> 형태로 반환한다.



- `BookSearchResponse findBookSearchResponse(BookSearchRequest bookSearchRequest)`

  - 검색 종류(type)과 검색어(keyword)를 담은 BookSearchRequest 객체를 받아서

    검색 기준을 충족하는 도서들의 정보를 BookSearchResponse 형태로 반환한다.





### CategoryService

- `List<CategoryBriefDTOWithChildren> findParentCategoriesWithChildren() `
  - 모든 카테고리 정보를 반환할 때 사용하는 메서드다.



- `CategoryDetailDTO findCategoryDetailById(Long id)`
  - 카테고리 id를 받아서 해당 카테고리의 상세 정보를 CategoryDetailDTO로 반환한다.



- `List<CategoryBriefDTO> findParentCategories()`
  - 가장 최상위의 카테고리들만 List\<CategoryBriefDTO> 형태로 반환한다.





### OrderService

- `OrderDetailResponse getOrderDetailResponse(List<CartInfoDTO> cartDTOs)`
  - 장바구니에 담긴 정보들을 파라미터로 받아서 주문에 필요한 정보들을 OrderDetailResponse로 반환한다.



- `void createOrder(OrderRegisterDTO orderRegisterDTO)`
  - 주문자 id, 주문할 책들 정보, 배송지 정보들을 OrderRegisterDTO로 받아서 주문을 처리하는 메서드다.



- `void cancelOrder(Long orderId)`
  - 주문 id를 받아서 해당 주문을 취소하는 메서드다.



- `OrderHistoryListResponse findOrderHistoryListResponse(Long userId, Pageable pageable, String orderBy, String status)`
  - 어떤 유저가 자신의 주문 내역을 조회할 때 사용하는 메서드다.
  - 유저 id, 페이징 정보, 정렬 기준, 주문 상태를 받아서 조건에 만족하는 주문 정보들을 OrderHistoryListResponse 형태로 반환한다.



- `OrderHistoryDetailResponse findOrderHistoryDetailResponse(Long orderId)`
  - 특정 주문의 상세 정보를 조회하고 싶을 때 사용하는 메서드다.
  - 주문 id를 받아서 OrderHistoryDetailResponse 형태로 반환한다.





### ReviewService

- `void post(ReviewPostDTO postDTO)`
  - 리뷰 등록자, 등록 대상 도서, 리뷰 이름, 리뷰 평점, 리뷰 내용을 담은 ReviewPostDTO를 받아서 Review를 등록하는 메서드다.



- `ReviewByUserDTO findReviewsByUser(Long userId)`
  - 어떤 User가 쓴 모든 Review를 조회하고 싶을 때 사용하는 메서드다.



- `void delete(ReviewDeleteDTO deleteDTO)`
  - 리뷰 등록자, 등록 대상 도서 정보를 담은 ReviewDeleteDTO를 받아서 해당 Review를 삭제하는 메서드다.





### UserService

- `Long register(UserRegisterDTO registerDTO)`
  - 회원 가입을 처리하는 메서드다.



- `UserDetailDTO findUserDetailDTO(Long id)`
  - 회원의 id를 받아서 회원의 상세정보를 담은 UserDetailDTO를 반환하는 메서드다.



- `void modifyUser(Long id, UserModifyDTO modifyDTO) `
  - 회원의 id와 수정하고 싶은 정보들을 담은 UserModifyDTO를 받아서 회원 정보 수정을 처리해주는 메서드다.



- `void addBookToWishList(Long userId, List<Long> bookIds) `

  - 회원의 id와 wishlist에 추가하고 싶은 book id들을 받아서 해당 book들을 wishlist에 추가해준다.

  

- `void deleteBooksInWishList(Long userId, List<Long> bookIds)`
  - 회원의 id와 wishlist에서 삭제하고 싶은 book id들을 받아서 해당 book들을 wishlist에서 삭제해준다.



- `BookWishlistResponse findBookWishlistResponse(Long userId) `
  - 회원의 id를 받아서 해당 회원의 Wishlist에 속한 도서들을 BookWishlistResponse 형태로 반환한다.



