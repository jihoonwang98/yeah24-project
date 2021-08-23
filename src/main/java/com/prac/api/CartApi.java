package com.prac.api;

import com.prac.auth.PrincipalDetails;
import com.prac.dto.BookDTO;
import com.prac.dto.BookDTO.BookCartDTO;
import com.prac.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cart")
@Slf4j
@RequiredArgsConstructor
public class CartApi {

    private final BookService bookService;

    /**
     * 장바구니에 들어있는 도서 목록 조회
     */
    @GetMapping
    public ResponseEntity<List<BookCartDTO>> getBooksInCart(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        List<Long> bookIds = principalDetails.getUser().getCarts().stream().collect(Collectors.toList());
        List<BookCartDTO> booksInCart = bookService.findBooksInCart(bookIds);

        return ResponseEntity.ok(booksInCart);
    }


    /**
     * 장바구니에 도서 추가
     */
    @PostMapping
    public ResponseEntity<String> addToCart(@RequestBody List<Long> bookIds, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        log.info("bookIds: " + bookIds.toString());

        principalDetails.addBooksToCart(bookIds);

        return ResponseEntity.ok("bookIds set");
    }


    /**
     * 장바구니에서 도서 삭제
     */
    @DeleteMapping
    public ResponseEntity<String> deleteInCart(@RequestBody Long bookId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        log.info("bookIds " + bookId);

        principalDetails.removeBookInCart(bookId);

        return ResponseEntity.ok("book deleted in cart");
    }

}
