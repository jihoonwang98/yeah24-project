package com.prac.repository.book;

import com.prac.domain.*;
import com.prac.domain.joinTable.BookAuthor;
import com.prac.domain.joinTable.Review;
import com.prac.dto.BookDTO.*;
import com.prac.dto.CategoryDTO.CategoryBriefDTOWithChildren;
import com.prac.dto.PageResultDTO;
import com.prac.dto.ReviewDTO.ReviewBriefDTO;
import com.prac.error.exception.notFound.BookNotFoundException;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.prac.domain.QAuthor.author;
import static com.prac.domain.QBook.book;
import static com.prac.domain.QCategory.category;
import static com.prac.domain.QUser.user;
import static com.prac.domain.joinTable.QBookAuthor.bookAuthor;
import static com.prac.domain.joinTable.QBookCategory.bookCategory;
import static com.prac.domain.joinTable.QReview.review;

@RequiredArgsConstructor
@Transactional
public class BookRepositoryImpl implements BookRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    @Override
    public BookListResponse findBooksByCategory(Long categoryId, Pageable pageable, String orderBy) {

        CategoryBriefDTOWithChildren categoryDTO = getCategoryBriefDTOWithChildren(categoryId);


        List<Book> books = queryFactory
                .select(book)
                .from(book)
                .where(book.bookCategories.any().id.categoryId.eq(categoryId))
                .orderBy(orderSpecifier(orderBy))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<BookListDTO> bookListDTOS = getBookListDTOs(books);

        Long totalCount = queryFactory
                .select(book.count())
                .from(book)
                .where(book.bookCategories.any().id.categoryId.eq(categoryId))
                .fetchOne();

        PageResultDTO<BookListDTO> pageResultDTO = new PageResultDTO<>(new PageImpl<>(bookListDTOS, pageable, totalCount));

        return new BookListResponse(categoryDTO, bookListDTOS, pageResultDTO);
    }


    @Override
    public BookDetailResponse findBookDetailById(Long bookId) {


        Book bookEntity = queryFactory
                .selectFrom(book).distinct()
                .join(book.bookCategories, bookCategory).fetchJoin()
                .join(bookCategory.category, category).fetchJoin()
                .where(book.id.eq(bookId))
                .fetchOne();


        if(bookEntity == null) {
            throw new BookNotFoundException();
        }


        List<Category> categories = bookEntity.getBookCategories().stream().map(bc -> bc.getCategory()).collect(Collectors.toList());

        List<Author> authors = queryFactory
                .select(author)
                .from(bookAuthor)
                .join(bookAuthor.author, author)
                .where(bookAuthor.id.bookId.eq(bookId))
                .fetch();

        List<Review> reviews = queryFactory
                .selectFrom(review)
                .join(review.user, user).fetchJoin()
                .where(review.id.bookId.eq(bookId))
                .fetch();


        return new BookDetailResponse(bookEntity, reviews, authors, categories);
    }

    @Override
    public BookWishlistResponse findBookWishlistResponse(Long userId) {

        User user = queryFactory
                .select(QUser.user)
                .from(QUser.user)
                .leftJoin(QUser.user.wishlist).fetchJoin()
                .where(QUser.user.id.eq(userId))
                .fetchOne();

        Set<Book> wishlist = user.getWishlist();

        return new BookWishlistResponse(user, wishlist.stream().collect(Collectors.toList()));
    }

    @Override
    public List<BookListDTO> findNewBooks(Long count) {

        List<Book> books = queryFactory
                .selectFrom(book)
                .orderBy(book.publicationDate.desc())
                .limit(count)
                .fetch();

        List<BookListDTO> bookListDTOS = getBookListDTOs(books);

        return bookListDTOS;
    }



    @Override
    public List<BookCartDTO> findBooksInCart(List<Long> bookIds) {
        return queryFactory
                .select(Projections.constructor(BookCartDTO.class, book))
                .from(book)
                .where(book.id.in(bookIds))
                .fetch();
    }

    @Override
    public BookSearchResponse findBookSearchResponse(String type, String keyword, Pageable pageable, String orderBy) {

        if(type.equals("name") || type.equals("bio")) {
            List<Book> books = queryFactory
                    .selectFrom(book)
                    .where(getPredicate(type, keyword))
                    .orderBy(orderSpecifier(orderBy))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();

            List<BookListDTO> bookListDTOs = getBookListDTOs(books);

            Long totalCount = queryFactory
                    .select(book.count())
                    .from(book)
                    .where(getPredicate(type, keyword))
                    .fetchOne();

            PageResultDTO<BookListDTO> pageResult = new PageResultDTO<>(new PageImpl<>(bookListDTOs, pageable, totalCount));

            return new BookSearchResponse(bookListDTOs, pageResult, type, keyword);
        }

        else if(type.equals("author")) {

            List<Book> books = queryFactory
                    .select(bookAuthor.book)
                    .from(bookAuthor)
                    .join(bookAuthor.book, book)
                    .join(bookAuthor.author, author)
                    .where(bookAuthorContains(keyword))
                    .groupBy(bookAuthor.book.id)
                    .orderBy(orderSpecifier(orderBy))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();

            List<BookListDTO> bookListDTOs = getBookListDTOs(books);

            Long totalCount = queryFactory
                    .select(bookAuthor.book.count())
                    .from(bookAuthor)
                    .join(bookAuthor.book, book)
                    .join(bookAuthor.author, author)
                    .where(bookAuthorContains(keyword))
                    .groupBy(bookAuthor.book.id)
                    .fetchOne();

            PageResultDTO<BookListDTO> pageResult = new PageResultDTO<>(new PageImpl<>(bookListDTOs, pageable, totalCount));

            return new BookSearchResponse(bookListDTOs, pageResult, type, keyword);
        }

        return null;
    }

    private Predicate getPredicate(String type, String keyword) {
        switch (type) {
            case "name":
                return bookNameContains(keyword);

            case "author":
                return bookAuthorContains(keyword);

            case "bio":
                return bookBioContains(keyword);
        }
        return null;
    }

    private Predicate bookNameContains(String keyword) {
        return (StringUtils.hasText(keyword)) ? book.name.contains(keyword) : null;
    }

    private Predicate bookAuthorContains(String keyword) {
        return (StringUtils.hasText(keyword)) ? bookAuthor.author.name.contains(keyword) : null;
    }

    private Predicate bookBioContains(String keyword) {
        return (StringUtils.hasText(keyword)) ? book.bio.contains(keyword) : null;
    }


    private OrderSpecifier<?> orderSpecifier(String orderBy) {
        if (!StringUtils.hasText(orderBy)) {
            return basicOrder();
        }

        switch (orderBy) {
            case "price":
                return bookPriceAsc();

            case "new":
                return bookPubDateDesc();

            default:
                return basicOrder();
        }
    }


    private OrderSpecifier<?> bookPriceAsc() {
        return book.price.asc();
    }

    private OrderSpecifier<?> bookPubDateDesc() {
        return book.publicationDate.desc();
    }

    private OrderSpecifier<?> basicOrder() {
        return book.regDate.asc();
    }



    private CategoryBriefDTOWithChildren getCategoryBriefDTOWithChildren(Long categoryId) {
        Category cateEntity = em.createQuery("select distinct c from Category c left join fetch c.children where c.id =:id", Category.class)
                .setParameter("id", categoryId)
                .getSingleResult();

        return new CategoryBriefDTOWithChildren(cateEntity);
    }


    private Map<Long, ReviewBriefDTO> getReviewBriefDTOMap(List<Long> bookIds) {
        List<ReviewBriefDTO> results =
                queryFactory
                        .select(Projections.constructor(ReviewBriefDTO.class, review.id.bookId, review.count(), review.rating.avg()))
                        .from(review)
                        .where(review.id.bookId.in(bookIds))
                        .groupBy(review.id.bookId)
                        .fetch();

        Map<Long, ReviewBriefDTO> map = results.stream().collect(
                Collectors.toMap(dto -> dto.getBookId(),
                dto -> dto));

        return map;
    }


    private Map<Long, List<Author>> getAuthorListMap(List<Long> bookIds) {
        List<BookAuthor> bookAuthors = queryFactory
                .selectFrom(bookAuthor)
                .join(bookAuthor.author, author).fetchJoin()
                .where(bookAuthor.id.bookId.in(bookIds))
                .fetch();

        Map<Long, List<Author>> authorMap = bookAuthors.stream().collect(Collectors.groupingBy(
                ba -> ba.getId().getBookId(),
                Collectors.mapping(BookAuthor::getAuthor, Collectors.toList())));

        return authorMap;
    }


    private List<BookListDTO> getBookListDTOs(List<Book> books) {
        List<Long> bookIds = books.stream().map(b -> b.getId()).collect(Collectors.toList());

        Map<Long, ReviewBriefDTO> reviewMap = getReviewBriefDTOMap(bookIds);

        Map<Long, List<Author>> authorMap = getAuthorListMap(bookIds);

        List<BookListDTO> bookListDTOS = books.stream()
                .map(b -> new BookListDTO(b, authorMap.get(b.getId()), reviewMap.get(b.getId())))
                .collect(Collectors.toList());

        return bookListDTOS;
    }


}
