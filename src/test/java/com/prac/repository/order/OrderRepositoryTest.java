package com.prac.repository.order;

import com.prac.config.BasicConfiguration;
import com.prac.domain.Author;
import com.prac.domain.Book;
import com.prac.domain.Order;
import com.prac.domain.Order.OrderInfoVO;
import com.prac.domain.User;
import com.prac.domain.etc.Address;
import com.prac.domain.joinTable.OrderBook;
import com.prac.dto.BookDTO;
import com.prac.dto.BookDTO.BookOrderDTO;
import com.prac.dto.CartDTO;
import com.prac.dto.CartDTO.CartInfoDTO;
import com.prac.dto.OrderDTO;
import com.prac.dto.OrderDTO.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(BasicConfiguration.class)
class OrderRepositoryTest {

    @Autowired
    TestEntityManager em;

    @Autowired
    OrderRepository orderRepository;

    User user;

    Author author;

    Book book1;
    Book book2;
    Book book3;

    Order order;
    Order order2;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .username("user")
                .name("user")
                .nickname("user")
                .address(new Address("postcode", "road", "jibun", "detail", "extra"))
                .build();

        author = new Author("author", "bio");

        book1 = Book.builder()
                .authors(new ArrayList<Author>() {
                    {
                        add(author);
                    }
                })
                .price(10000)
                .name("book1")
                .stockQuantity(10)
                .build();

        book2 = Book.builder()
                .authors(new ArrayList<Author>() {
                    {
                        add(author);
                    }
                })
                .price(20000)
                .name("book2")
                .stockQuantity(15)
                .build();

        book3 = Book.builder()
                .authors(new ArrayList<Author>() {
                    {
                        add(author);
                    }
                })
                .price(30000)
                .name("book3")
                .stockQuantity(8)
                .build();

        List<OrderInfoVO> orderInfos = Arrays.asList(new OrderInfoVO(book1, 5), new OrderInfoVO(book2, 2), new OrderInfoVO(book3, 9));

        order = Order.createOrder(user, orderInfos, user.getAddress());


        orderInfos = Arrays.asList(new OrderInfoVO(book1, 1), new OrderInfoVO(book2, 2));
        order2 = Order.createOrder(user, orderInfos, user.getAddress());


        em.persist(user);
        em.persist(author);
        em.persist(book1);
        em.persist(book2);
        em.persist(book3);
        em.persist(order);
        em.persist(order2);
    }

    @Test
    void getOrderDetailResponse() {
        CartInfoDTO cartInfoDTO1 = new CartInfoDTO();
        cartInfoDTO1.setBookId(book1.getId());
        cartInfoDTO1.setAmount(4);
        cartInfoDTO1.setPriceSum(4 * book1.getPrice());

        CartInfoDTO cartInfoDTO2 = new CartInfoDTO();
        cartInfoDTO2.setBookId(book2.getId());
        cartInfoDTO2.setAmount(2);
        cartInfoDTO2.setPriceSum(2 * book2.getPrice());

        OrderDetailResponse orderDetailResponse = orderRepository.getOrderDetailResponse(Arrays.asList(
                cartInfoDTO1,
                cartInfoDTO2
        ));

        assertThat(orderDetailResponse.getTotalOrderPrice()).isEqualTo(cartInfoDTO1.getPriceSum() + cartInfoDTO2.getPriceSum());

        List<OrderDetailDTO> orders = orderDetailResponse.getOrders();
        assertThat(orders).hasSize(2);
        assertThat(orders).containsExactlyInAnyOrder(
                new OrderDetailDTO(new BookOrderDTO(book1), 4, cartInfoDTO1.getPriceSum()),
                new OrderDetailDTO(new BookOrderDTO(book2), 2, cartInfoDTO2.getPriceSum()));

    }

    @Test
    void getOrderForCancel() {
        Order orderFind = orderRepository.getOrderForCancel(order.getId());
        System.out.println(orderFind);

        List<OrderBook> orderBooks = orderFind.getOrderBooks();
        for (OrderBook orderBook : orderBooks) {
            System.out.println("orderBook.getBook().getId() = " + orderBook.getBook().getId());
        }
    }

    @Test
    void findOrderHistoryListResponse() {

        OrderHistoryListResponse response = orderRepository.findOrderHistoryListResponse(user.getId(), PageRequest.of(0, 10), null, "all");
        List<OrderHistoryListDTO> orders = response.getOrders();
        assertThat(orders).hasSize(2);

        OrderHistoryListDTO order1History = orders.stream().filter(o -> o.getOrderId().equals(order.getId())).findFirst().get();
        OrderHistoryListDTO order2History = orders.stream().filter(o -> o.getOrderId().equals(order2.getId())).findFirst().get();

        assertThat(order1History.getTotalOrderPrice()).isEqualTo(book1.getPrice() * 5 + book2.getPrice() * 2 + book3.getPrice() * 9);
        assertThat(order1History.getTotalAmount()).isEqualTo(5 + 2 + 9);
        assertThat(order1History.getOrderBookCount()).isEqualTo(3);

        assertThat(order2History.getTotalOrderPrice()).isEqualTo(book1.getPrice() + book2.getPrice() * 2);
        assertThat(order2History.getTotalAmount()).isEqualTo(1+2);
        assertThat(order2History.getOrderBookCount()).isEqualTo(2);
    }

    @Test
    void findOrderHistoryDetailResponse() {

        OrderHistoryDetailResponse orderHistoryDetailResponse = orderRepository.findOrderHistoryDetailResponse(order.getId());

        assertThat(orderHistoryDetailResponse.getOrderId()).isEqualTo(order.getId());
        assertThat(orderHistoryDetailResponse.getOrderer()).isEqualTo(order.getOrderer().getName());
        assertThat(orderHistoryDetailResponse.getTotalOrderPrice()).isEqualTo(book1.getPrice() * 5 + book2.getPrice() * 2 + book3.getPrice() * 9);
        assertThat(orderHistoryDetailResponse.getDeliveryAddr()).isEqualTo(user.getAddress());

        List<OrderHistoryDetailDTO> orders = orderHistoryDetailResponse.getOrders();
        assertThat(orders).hasSize(3);
        assertThat(orders).hasSameElementsAs(order.getOrderBooks().stream().map(ob -> new OrderHistoryDetailDTO(ob)).collect(Collectors.toList()));
    }
}