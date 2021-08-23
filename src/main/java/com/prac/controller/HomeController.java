package com.prac.controller;

import com.prac.auth.PrincipalDetails;
import com.prac.dto.AuthorDTO.AuthorDetailDTO;
import com.prac.dto.BookDTO.*;
import com.prac.dto.CartDTO;
import com.prac.dto.CartDTO.CartInfoDTO;
import com.prac.dto.CartDTO.CartInfoListDTO;
import com.prac.dto.CategoryDTO.CategoryBriefDTO;
import com.prac.dto.OrderDTO;
import com.prac.dto.OrderDTO.*;
import com.prac.dto.PageRequestDTO;
import com.prac.dto.ReviewDTO.ReviewByUserDTO;
import com.prac.dto.UserDTO.UserDetailDTO;
import com.prac.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {

    private final BookService bookService;
    private final AuthorService authorService;
    private final UserService userService;
    private final ReviewService reviewService;
    private final OrderService orderService;

    @GetMapping("/")
    public String home(Model model) {

        List<BookListDTO> newBooks = bookService.findNewBooks();
        model.addAttribute("newBooks", newBooks);
        return "index";
    }


    @GetMapping("/categories/{id}/books")
    public String booksInCategory(@PathVariable("id") Long id, @ModelAttribute("pageRequest") PageRequestDTO pageRequestDTO, Model model) {
        BookListResponse bookListResponse = bookService.findBooksByCategoryId(id, pageRequestDTO.of(), pageRequestDTO.getOrderBy());
        model.addAttribute("bookListResponse", bookListResponse);
        return "category-detail";
    }


    @GetMapping("/books/{id}")
    public String bookDetail(@PathVariable("id") Long id, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        BookDetailResponse bookDetailResponse = bookService.findBookDetailByBookId(id);
        bookDetailResponse.getCategories().stream().sorted(Comparator.comparingInt(CategoryBriefDTO::getDepth));

        model.addAttribute("bookDetailResponse", bookDetailResponse);

        if(principalDetails != null) {
            model.addAttribute("reviewExists", bookDetailResponse.hasUserReview(principalDetails.getUser().getId()));
        }

        return "book-detail";
    }


    @GetMapping("/books/search")
    public String bookSearch(@ModelAttribute("bookSearch") BookSearchRequest bookSearchRequest, Model model) {
        log.info("bookSearchRequest: " + bookSearchRequest);

        BookSearchResponse bookSearchResponse = bookService.findBookSearchResponse(bookSearchRequest);
        model.addAttribute("bookSearchResponse", bookSearchResponse);

        return "book-search";
    }


    @GetMapping("/authors/{id}")
    public String authorDetail(@PathVariable("id") Long id, Model model) {
        AuthorDetailDTO authorDetailDTO = authorService.findAuthorDetailById(id);
        model.addAttribute("authorDetailDTO", authorDetailDTO);
        return "author-detail";
    }


    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/users/{id}")
    public String userDetail(@PathVariable("id") Long id, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        log.info("principalDetails: " + principalDetails);

        if (principalDetails.getUser().getId() != id) {
            log.info("다른 User가 회원 정보를 수정하려고 함.");
            return "auth/access-denied";
        }

        UserDetailDTO userDetailDTO = userService.findUserDetailDTO(id);
        model.addAttribute("modifyDTO", userDetailDTO);

        return "user-detail";
    }


    
    @GetMapping("/users/{id}/reviews")
    public String reviewsWrittenByUser(@PathVariable("id") Long id, Model model) {
        log.info("GET /users/" + id + "/reviews");

        ReviewByUserDTO reviewByUserDTO = reviewService.findReviewsByUser(id);
        model.addAttribute("reviewByUserDTO", reviewByUserDTO);

        return "reviews-by-user";
    }

    @GetMapping("/users/{id}/wishlist")
    public String wishlist(@PathVariable("id") Long id, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        log.info("principalDetails: " + principalDetails);

        if(principalDetails != null && principalDetails.getUser().getId().equals(id)) {
            model.addAttribute("myself", true);
        }

        BookWishlistResponse response = userService.findBookWishlistResponse(id);
        model.addAttribute("bookWishlistResponse", response);
        model.addAttribute("userId", id);
        return "wishlist";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/users/{id}/orders")
    public String orderHistory(@PathVariable("id") Long id, Model model, @ModelAttribute("pageRequest") PageRequestDTO pageRequestDTO, @AuthenticationPrincipal PrincipalDetails principalDetails, @ModelAttribute("status") String status) {
        log.info("status: " + status);
        Long userId = principalDetails.getUser().getId();

        if (principalDetails.getUser().getId() != id) {
            log.info("다른 User가 주문 내역을 조회하려고 함.");
            return "auth/access-denied";
        }

        OrderHistoryListResponse response = orderService.findOrderHistoryListResponse(userId, pageRequestDTO.of(), pageRequestDTO.getOrderBy(), status);
        model.addAttribute("response", response);

        return "order-history";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/users/{userId}/orders/{orderId}")
    public String orderDetailHistory(@PathVariable("userId") Long userId, @PathVariable("orderId") Long orderId, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        log.info("userId: " + userId);
        log.info("orderId: " + orderId);

        if (principalDetails.getUser().getId() != userId) {
            log.info("다른 User가 주문 내역을 조회하려고 함.");
            return "auth/access-denied";
        }

        OrderHistoryDetailResponse response = orderService.findOrderHistoryDetailResponse(orderId);
        model.addAttribute("response", response);

        return "order-detail-history";
    }

    

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/cart")
    public String cart(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {

        List<Long> bookIds = principalDetails.getUser().getCarts().stream().collect(Collectors.toList());
        List<BookCartDTO> booksInCart = bookService.findBooksInCart(bookIds);
        model.addAttribute("books", booksInCart);

        return "cart";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/orders")
    public String orderPage(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model, CartInfoListDTO cartDTOList) {
        log.info("cartDTOList: " + cartDTOList);
        OrderDetailResponse orderDetailResponse = orderService.getOrderDetailResponse(cartDTOList.getBooks());
        model.addAttribute("orderResponse", orderDetailResponse);

        UserDetailDTO user = userService.findUserDetailDTO(principalDetails.getUser().getId());
        model.addAttribute("user", user);

        return "order";
    }


    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/orders")
    public String order(@AuthenticationPrincipal PrincipalDetails principalDetails, OrderRegisterDTO orderRegisterDTO, Model model) {
        log.info("orderRegisterDTO: " + orderRegisterDTO);
        orderService.createOrder(orderRegisterDTO);

        principalDetails.clearAllBooksInCart();

        return "redirect:/";
    }


}
