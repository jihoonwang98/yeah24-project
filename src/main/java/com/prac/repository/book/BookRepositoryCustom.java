package com.prac.repository.book;

import com.prac.dto.BookDTO;
import com.prac.dto.BookDTO.*;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookRepositoryCustom {

    BookListResponse findBooksByCategory(Long categoryId, Pageable pageable, String orderBy);

    BookDetailResponse findBookDetailById(Long bookId);

    BookWishlistResponse findBookWishlistResponse(Long userId);

    List<BookListDTO> findNewBooks(Long count);

    List<BookCartDTO> findBooksInCart(List<Long> bookIds);

    BookSearchResponse findBookSearchResponse(String type, String keyword, Pageable pageable, String orderBy);
}
