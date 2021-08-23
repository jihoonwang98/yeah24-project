package com.prac.controller;

import com.prac.auth.PrincipalDetailsService;
import com.prac.auth.PrincipalOauth2UserService;
import com.prac.domain.Author;
import com.prac.domain.Book;
import com.prac.domain.Category;
import com.prac.domain.User;
import com.prac.dto.AuthorDTO.AuthorBriefDTO;
import com.prac.dto.AuthorDTO.AuthorDetailDTO;
import com.prac.dto.BookDTO.*;
import com.prac.dto.CategoryDTO.CategoryBriefDTO;
import com.prac.dto.CategoryDTO.CategoryBriefDTOWithChildren;
import com.prac.dto.PageRequestDTO;
import com.prac.dto.PageResultDTO;
import com.prac.dto.ReviewDTO.ReviewBriefDTO;
import com.prac.dto.ReviewDTO.ReviewByUserDTO;
import com.prac.dto.SearchRequestDTO;
import com.prac.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = HomeController.class)
class HomeControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private UserService userService;

    @MockBean
    private ReviewService reviewService;

    @MockBean
    private OrderService orderService;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private PrincipalOauth2UserService principalOauth2UserService;

    @MockBean
    private PrincipalDetailsService principalDetailsService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    User user;

    Author author1;
    Author author2;
    Author author3;

    Book book1;
    Book book2;

    Category parent;
    Category child;
    Category grandChild;

    Map<String, List<CategoryBriefDTO>> parents;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .username("username")
                .name("name")
                .build();

        book1 = Book.builder()
                .name("book1")
                .authors(new ArrayList<>())
                .build();

        book2 = Book.builder()
                .name("book2")
                .authors(new ArrayList<>())
                .build();

        author1 = new Author("author1", "author1입니다.");
        author2 = new Author("author2", "author2입니다.");
        author3 = new Author("author3", "author3입니다.");

        parent = new Category("parent", null);
        child = new Category("child", parent);
        grandChild = new Category("grandChild", child);

        parents = new HashMap<String, List<CategoryBriefDTO>>() {{ put("categories", Arrays.asList(new CategoryBriefDTO(parent)));}};

        given(categoryService.findParentCategories()).willReturn(Arrays.asList(new CategoryBriefDTO(parent)));
    }



    @Test
    @WithAnonymousUser
    void home() throws Exception{
        // given
        List<BookListDTO> books = Arrays.asList(
                new BookListDTO(book1, new ArrayList<Author>() { {add(author1);}}, new ReviewBriefDTO(1L, 2L, 5.0)),
                new BookListDTO(book2, new ArrayList<Author>() { {add(author2); add(author3);}}, new ReviewBriefDTO(2L, 4L, 3.5))
        );

        given(bookService.findNewBooks()).willReturn(books);

        // when, then
        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(HomeController.class))
                .andExpect(handler().methodName("home"))
                .andExpect(view().name("index"))
                .andExpect(model().attribute("newBooks", books))
                .andExpect(model().attribute("parents", parents));

    }

    @Test
    @WithAnonymousUser
    void booksInCategory() throws Exception {
        PageRequestDTO pageRequestDTO = new PageRequestDTO(PageRequestDTO.DEFAULT_SIZE, 1);
        List<BookListDTO> books = Arrays.asList(
                new BookListDTO(book1, new ArrayList<Author>() { {add(author1);}}, new ReviewBriefDTO(1L, 2L, 5.0)),
                new BookListDTO(book2, new ArrayList<Author>() { {add(author2); add(author3);}}, new ReviewBriefDTO(2L, 4L, 3.5))
        );
        PageImpl<BookListDTO> page = new PageImpl<>(books, pageRequestDTO.of(), 2);
        BookListResponse bookListResponse = new BookListResponse(new CategoryBriefDTOWithChildren(child), books, new PageResultDTO<>(page));


        // given
        given(bookService.findBooksByCategoryId(1L, pageRequestDTO.of(), null)).willReturn(bookListResponse);

        // when, then
        mvc.perform(get("/categories/{id}/books", 1)
                        .param("size", pageRequestDTO.getSize() + "")
                        .param("page", pageRequestDTO.getPage() + ""))
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(HomeController.class))
                .andExpect(handler().methodName("booksInCategory"))
                .andExpect(view().name("category-detail"))
                .andExpect(model().attribute("bookListResponse", bookListResponse))
                .andExpect(model().attribute("parents", parents));
    }

    @Test
    @WithAnonymousUser
    void bookDetail() throws Exception {
        BookDetailDTO book = new BookDetailDTO(book1, new ArrayList<>());
        List<AuthorBriefDTO> authors = Arrays.asList(new AuthorBriefDTO(author1));
        List<CategoryBriefDTO> categories = Arrays.asList(new CategoryBriefDTO(parent));


        BookDetailResponse response = new BookDetailResponse(book, authors, categories);


        // given
        given(bookService.findBookDetailByBookId(1L)).willReturn(response);


        mvc.perform(get("/books/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(HomeController.class))
                .andExpect(handler().methodName("bookDetail"))
                .andExpect(view().name("book-detail"))
                .andExpect(model().attribute("bookDetailResponse", response));
    }

    @Test
    void bookSearch() throws Exception {
        BookSearchRequest bookSearchRequest = new BookSearchRequest();
        SearchRequestDTO searchRequestDTO = new SearchRequestDTO();
        searchRequestDTO.setKeyword("프로그래밍");
        searchRequestDTO.setType("name");

        PageRequestDTO pageRequestDTO = new PageRequestDTO();
        pageRequestDTO.setPage(1);
        pageRequestDTO.setSize(PageRequestDTO.DEFAULT_SIZE);
        pageRequestDTO.setOrderBy(null);

        List<BookListDTO> books = Arrays.asList(
                new BookListDTO(book1, new ArrayList<Author>() { {add(author1);}}, new ReviewBriefDTO(1L, 2L, 5.0)),
                new BookListDTO(book2, new ArrayList<Author>() { {add(author2); add(author3);}}, new ReviewBriefDTO(2L, 4L, 3.5))
        );

        PageResultDTO<BookListDTO> pageResult = new PageResultDTO<>(new PageImpl<>(books, pageRequestDTO.of(), 2L));

        BookSearchResponse bookSearchResponse = new BookSearchResponse(books, pageResult, searchRequestDTO.getType(), searchRequestDTO.getKeyword());

        // given
        given(bookService.findBookSearchResponse(any())).willReturn(bookSearchResponse);

        // when, then
        mvc.perform(get("/books/search")
                        .param("searchParam.type", searchRequestDTO.getType())
                        .param("searchParam.keyword", searchRequestDTO.getKeyword())
                        .param("pageParam.size", pageRequestDTO.getSize()+"")
                        .param("pageParam.page", pageRequestDTO.getPage()+"")
                        .param("pageParam.orderBy", pageRequestDTO.getOrderBy()))
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(HomeController.class))
                .andExpect(handler().methodName("bookSearch"))
                .andExpect(view().name("book-search"))
                .andExpect(model().attribute("bookSearchResponse", bookSearchResponse));
    }

    @Test
    void authorDetail() throws Exception {
        AuthorDetailDTO dto = new AuthorDetailDTO(author1, new ArrayList<Book>() {{
            add(book1);
            add(book2);
        }});


        // given
        given(authorService.findAuthorDetailById(1L)).willReturn(dto);


        // when, then
        mvc.perform(get("/authors/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(HomeController.class))
                .andExpect(handler().methodName("authorDetail"))
                .andExpect(view().name("author-detail"))
                .andExpect(model().attribute("authorDetailDTO", dto));
    }

    @Test
    void reviewsWrittenByUser() throws Exception {

        ReviewByUserDTO dto = new ReviewByUserDTO(user);

        // given
        given(reviewService.findReviewsByUser(1L)).willReturn(dto);


        // when, then
        mvc.perform(get("/users/{id}/reviews", 1L))
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(HomeController.class))
                .andExpect(handler().methodName("reviewsWrittenByUser"))
                .andExpect(view().name("reviews-by-user"))
                .andExpect(model().attribute("reviewByUserDTO", dto));
    }



    @Test
    @WithAnonymousUser
    void wishlist() throws Exception {
        BookWishlistResponse response = new BookWishlistResponse(user, new ArrayList<Book>() {{
            add(book1);
            add(book2);
        }});

        // given
        given(userService.findBookWishlistResponse(1L)).willReturn(response);


        // when, then
        mvc.perform(get("/users/{id}/wishlist", 1L))
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(HomeController.class))
                .andExpect(handler().methodName("wishlist"))
                .andExpect(view().name("wishlist"))
                .andExpect(model().attribute("bookWishlistResponse", response))
                .andExpect(model().attribute("userId", 1L));
    }



}