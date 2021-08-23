package com.prac.api;

import com.prac.IntegrationTest;
import com.prac.controller.HomeController;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthorApiTest extends IntegrationTest {


    @Test
    void findAuthorDetailSuccess() throws Exception {
        Long id = 1L;

        mvc.perform(get("/api/authors/{id}", id))
                .andExpect(status().isOk())
                .andExpect(handler().handlerType(AuthorApi.class))
                .andExpect(handler().methodName("findAuthorDetail"))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("서종원"))
                .andExpect(jsonPath("$.bio").isString())
                .andExpect(jsonPath("$.books.length()").value(1))
                .andExpect(jsonPath("$.books[0].id").value(1))
                .andExpect(jsonPath("$.books[0].name").value("로블록스 게임 제작 무작정 따라하기"));
    }


    @Test
    void findAuthorDetailNotFound() throws Exception {
        Long id = 100000000L;

        mvc.perform(get("/api/authors/{id}", id))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("Entity Not Found"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").value("C003"))
                .andExpect(jsonPath("$.errors.length()").value(0));
    }
}