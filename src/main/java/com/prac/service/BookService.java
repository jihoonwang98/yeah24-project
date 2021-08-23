package com.prac.service;

import com.prac.dto.BookDTO;
import com.prac.dto.BookDTO.*;
import com.prac.dto.PageRequestDTO;
import com.prac.dto.SearchRequestDTO;
import com.prac.repository.book.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BookService {

    private static final Long DEFAULT_NEW_BOOKS_SIZE = 5L;
    private final BookRepository bookRepository;

    public BookListResponse findBooksByCategoryId(Long id, Pageable pageable, String orderBy) {
        return bookRepository.findBooksByCategory(id, pageable, orderBy);
    }

    public BookDetailResponse findBookDetailByBookId(Long id) {
        return bookRepository.findBookDetailById(id);
    }

    public List<BookListDTO> findNewBooks() {
        return bookRepository.findNewBooks(DEFAULT_NEW_BOOKS_SIZE);
    }

    public List<BookCartDTO> findBooksInCart(List<Long> bookIds) {
        return bookRepository.findBooksInCart(bookIds);
    }


    public BookSearchResponse findBookSearchResponse(BookSearchRequest bookSearchRequest) {
        SearchRequestDTO searchParam = bookSearchRequest.getSearchParam();
        PageRequestDTO pageParam = bookSearchRequest.getPageParam();

        return bookRepository.findBookSearchResponse(searchParam.getType(), searchParam.getKeyword(), pageParam.of(), pageParam.getOrderBy());
    }

}
