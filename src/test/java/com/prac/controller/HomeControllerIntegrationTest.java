package com.prac.controller;

import com.prac.IntegrationTest;
import com.prac.WithMockCustomUser;
import com.prac.dto.BookDTO;
import com.prac.dto.CartDTO;
import com.prac.dto.CartDTO.CartInfoListDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

public class HomeControllerIntegrationTest extends IntegrationTest {


    @Test
    @WithMockCustomUser
    @DisplayName("자신의 User 정보에 접근하는 경우")
    void userDetail() throws Exception {

        mvc.perform(get("/users/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(HomeController.class))
                .andExpect(handler().methodName("userDetail"))
                .andExpect(view().name("user-detail"))
                .andExpect(model().attributeExists("parents", "modifyDTO"));

    }

    @Test
    @WithMockCustomUser
    @DisplayName("다른 User의 정보에 접근하는 경우")
    void userDetailWithAnotherUser() throws Exception {

        mvc.perform(get("/users/{id}", 2L))
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(HomeController.class))
                .andExpect(handler().methodName("userDetail"))
                .andExpect(view().name("auth/access-denied"))
                .andExpect(model().attributeExists("parents"))
                .andExpect(model().attributeDoesNotExist("modifyDTO"));

    }

    @Test
    @WithAnonymousUser
    @DisplayName("로그인을 안한 사용자가 User의 정보에 접근하는 경우")
    void userDetailWithNotLoginUser() throws Exception {

        mvc.perform(get("/users/{id}", 1L))
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().handlerType(HomeController.class))
                .andExpect(handler().methodName("userDetail"));
    }


    @Test
    @WithMockCustomUser
    @DisplayName("자신의 주문 내역을 조회하는 경우")
    void orderHistory() throws Exception {

        mvc.perform(get("/users/{id}/orders", 1L))
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(HomeController.class))
                .andExpect(handler().methodName("orderHistory"))
                .andExpect(view().name("order-history"))
                .andExpect(model().attributeExists("parents", "response"));
    }

    @Test
    @WithMockCustomUser
    @DisplayName("다른 User의 주문 내역을 조회하는 경우")
    void orderHistoryWithAnotherUser() throws Exception {

        mvc.perform(get("/users/{id}/orders", 2L))
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(HomeController.class))
                .andExpect(handler().methodName("orderHistory"))
                .andExpect(view().name("auth/access-denied"))
                .andExpect(model().attributeExists("parents"))
                .andExpect(model().attributeDoesNotExist("response"));
    }


    @Test
    @WithMockCustomUser
    void cart() throws Exception {

        mvc.perform(get("/cart"))
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(HomeController.class))
                .andExpect(handler().methodName("cart"))
                .andExpect(view().name("cart"))
                .andExpect(model().attributeExists("parents", "books"));
    }

    @Test
    @WithMockCustomUser
    void orderPage() throws Exception {
        mvc.perform(get("/orders")
                    .param("books[0].bookId", "1")
                    .param("books[0].amount", "3")
                    .param("books[0].priceSum", "30000"))
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(HomeController.class))
                .andExpect(handler().methodName("orderPage"))
                .andExpect(view().name("order"))
                .andExpect(model().attributeExists("parents", "orderResponse", "user"));
    }

    @Test
    @WithMockCustomUser
    void orderProcess() throws Exception {
        mvc.perform(post("/orders")
                    .param("userId", "1")
                    .param("books[0].bookId", "1")
                    .param("books[0].amount", "3")
                    .param("books[0].priceSum", "30000")
                    .param("deliveryAddress.postcode", "postcode")
                    .param("deliveryAddress.roadAddr", "roadAddr")
                    .param("deliveryAddress.jibunAddr", "jibunAddr")
                    .param("deliveryAddress.detailAddr", "detailAddr")
                    .param("deliveryAddress.extraAddr", "extraAddr"))
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().handlerType(HomeController.class))
                .andExpect(handler().methodName("order"))
                .andExpect(view().name("redirect:/"));
    }
}
