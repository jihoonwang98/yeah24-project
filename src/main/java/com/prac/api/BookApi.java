package com.prac.api;

import com.prac.dto.BookDTO;
import com.prac.dto.BookDTO.BookDetailResponse;
import com.prac.dto.BookDTO.BookListResponse;
import com.prac.dto.BookDTO.BookSearchRequest;
import com.prac.dto.BookDTO.BookSearchResponse;
import com.prac.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/books")
public class BookApi {

    private final BookService bookService;

    /**
     * 도서 상세 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<BookDetailResponse> findBookDetailByBookId(@PathVariable("id") Long id) {
        BookDetailResponse response = bookService.findBookDetailByBookId(id);
        return ResponseEntity.ok(response);
    }

    /**
     * 검색 조건에 해당하는 도서들 조회
     */
    @GetMapping("/search")
    public ResponseEntity<BookSearchResponse> findBooksBySearch(@Valid @RequestBody BookSearchRequest bookSearchRequest) {
        BookSearchResponse bookSearchResponse = bookService.findBookSearchResponse(bookSearchRequest);
        return ResponseEntity.ok(bookSearchResponse);
    }


}
