package com.prac.dto;

import com.prac.domain.Author;
import com.prac.domain.Book;
import com.prac.domain.Category;
import com.prac.domain.User;
import com.prac.domain.joinTable.Review;
import com.prac.dto.AuthorDTO.AuthorBriefDTO;
import com.prac.dto.CategoryDTO.CategoryBriefDTO;
import com.prac.dto.CategoryDTO.CategoryBriefDTOWithChildren;
import com.prac.dto.ReviewDTO.ReviewBriefDTO;
import com.prac.dto.ReviewDTO.ReviewDetailDTO;
import com.prac.dto.ReviewDTO.ReviewIntegrationDTO;
import com.prac.dto.UserDTO.UserWishlistDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class BookDTO {

    @Data
    public static class BookListDTO {

        private Long id; // book
        private String name;  // book
        private int price;    // book
        private String bio;   // book
        public LocalDate publicationDate; // book;
        private String imgSrc; // book
        private int stockQuantity; // book

        private ReviewBriefDTO review; // review
        private List<AuthorBriefDTO> authors; // author

        public BookListDTO(Book book, List<Author> authors, ReviewBriefDTO reviewDTO) {
            this.id = book.getId();
            this.name = book.getName();
            this.price = book.getPrice();
            this.publicationDate = book.getPublicationDate();
            this.bio = book.getBio();
            this.imgSrc = book.getImgSrc();
            this.stockQuantity = book.getStockQuantity();
            this.review = reviewDTO;
            this.authors = authors.stream().map(a -> new AuthorBriefDTO(a)).collect(Collectors.toList());
        }
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BookListResponse {
        private CategoryBriefDTOWithChildren category;
        private List<BookListDTO> books;
        private PageResultDTO<BookListDTO> pageResult;
    }



    @Data
    public static class BookDetailDTO {
        private Long id;  // book
        private LocalDate publicationDate; // book
        private String name; // book
        private int price;  // book
        private String isbn; // book
        private String bio; // book
        private String imgSrc;
        private int stockQuantity;

        private ReviewIntegrationDTO review;

        public BookDetailDTO(Book book, List<Review> reviews) {
            this.id = book.getId();
            this.publicationDate = book.getPublicationDate();
            this.name = book.getName();
            this.price = book.getPrice();
            this.isbn = book.getIsbn();
            this.bio = book.getBio();
            this.stockQuantity = book.getStockQuantity();
            this.imgSrc = book.getImgSrc();
            this.review = new ReviewIntegrationDTO(reviews);
        }
    }


    @Data
    public static class BookAuthorDTO {
        private Long id;  // book
        private LocalDate publicationDate; // book
        private String name; // book
        private int price;  // book
        private String isbn; // book
        private String bio; // book
        private String imgSrc; // book

        public BookAuthorDTO(Book book) {
            this.id = book.getId();
            this.publicationDate = book.getPublicationDate();
            this.name = book.getName();
            this.price = book.getPrice();
            this.isbn = book.getIsbn();
            this.bio = book.getBio();
            this.imgSrc = book.getImgSrc();
        }
    }



    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookDetailResponse {
        private BookDetailDTO book;
        private List<AuthorBriefDTO> authors;
        private List<CategoryBriefDTO> categories;

        public BookDetailResponse(Book book, List<Review> reviews, List<Author> authors, List<Category> categories) {
            this.book = new BookDetailDTO(book, reviews);
            this.authors = authors.stream().map(a -> new AuthorBriefDTO(a)).collect(Collectors.toList());
            this.categories = categories.stream().map(c -> new CategoryBriefDTO(c)).collect(Collectors.toList());
        }


        public boolean hasUserReview(Long userId) {
            if (this.book.review.getReviews() == null || this.book.review.getReviews().size() == 0) {
                return false;
            }

            List<ReviewDetailDTO> reviews = this.book.review.getReviews();
            for (ReviewDetailDTO review : reviews) {
                if (review.getUser().getUserId().equals(userId)) {
                    return true;
                }
            }
            return false;
        }
    }



    @Data
    public static class BookWishlistDTO {
        private Long id;  // book
        private LocalDate publicationDate; // book
        private String name; // book
        private int price;  // book
        private String isbn; // book
        private String bio; // book
        private String imgSrc; // book

        public BookWishlistDTO(Book book) {
            this.id = book.getId();
            this.publicationDate = book.getPublicationDate();
            this.name = book.getName();
            this.price = book.getPrice();
            this.isbn = book.getIsbn();
            this.bio = book.getBio();
            this.imgSrc = book.getImgSrc();
        }
    }


    @Data
    public static class BookWishlistResponse {
        private UserWishlistDTO user;
        private List<BookWishlistDTO> books;

        public BookWishlistResponse(User user, List<Book> books) {
            this.user = new UserWishlistDTO(user);
            this.books = books.stream().map(b -> new BookWishlistDTO(b)).collect(Collectors.toList());
        }
    }

    @Data
    public static class BookCartDTO {
        private Long bookId;
        private String imgSrc;
        private String name;
        private int price;
        private int stockQuantity;

        public BookCartDTO(Book book) {
            this.bookId = book.getId();
            this.imgSrc = book.getImgSrc();
            this.name = book.getName();
            this.price = book.getPrice();
            this.stockQuantity = book.getStockQuantity();
        }
    }

    @Data
    public static class BookOrderDTO {
        private Long bookId;
        private String imgSrc;
        private String name;
        private int price;

        public BookOrderDTO(Book book) {
            this.bookId = book.getId();
            this.imgSrc = book.getImgSrc();
            this.name = book.getName();
            this.price = book.getPrice();
        }
    }

    @Data
    public static class BookOrderRegisterDTO {
        private Long bookId;
        private int amount;
        private int orderPrice;
    }


    @Data
    @AllArgsConstructor
    public static class BookSearchResponse {
        private List<BookListDTO> books;
        private PageResultDTO<BookListDTO> pageResult;
        private String type;
        private String keyword;
    }


    @Data
    public static class BookSearchRequest {

        @Valid
        private SearchRequestDTO searchParam;

        @NotNull
        private PageRequestDTO pageParam;
    }
}
