package com.prac.domain;

import com.prac.domain.Order.OrderInfoVO;
import com.prac.domain.etc.Address;
import com.prac.domain.etc.OrderStatus;
import com.prac.domain.joinTable.OrderBook;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OrderTest {

    User user1 = User.builder()
            .name("user1")
            .build();


    Book book1 = Book.builder()
            .name("book1")
            .authors(new ArrayList<>())
            .build();


    Book book2 = Book.builder()
            .name("book2")
            .authors(new ArrayList<>())
            .build();


    @Test
    void test1() {

        List<OrderInfoVO> orderInfoVOS = Arrays.asList(new OrderInfoVO(book1, 3), new OrderInfoVO(book2, 4));

        Order order = Order.createOrder(user1, orderInfoVOS, new Address(null, null, null, null, null));

        List<OrderBook> orderBooks = order.getOrderBooks();

        assertThat(order.getOrderer()).isEqualTo(user1);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.ORDER);
        assertThat(orderBooks).hasSize(2);

        for (OrderBook orderBook : orderBooks) {
            assertThat(orderBook.getOrder()).isEqualTo(order);
            assertThat(orderBook.getBook()).isNotNull();
        }
    }
}