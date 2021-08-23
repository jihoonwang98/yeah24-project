package com.prac.dto;

import com.prac.domain.Order;
import com.prac.domain.etc.Address;
import com.prac.domain.etc.OrderStatus;
import com.prac.domain.joinTable.OrderBook;
import com.prac.dto.BookDTO.BookOrderDTO;
import com.prac.dto.BookDTO.BookOrderRegisterDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.reducing;

public class OrderDTO {

    @Data
    public static class OrderDetailDTO {
        private BookOrderDTO book;
        private int amount;
        private int totalPrice;

        public OrderDetailDTO(BookOrderDTO book, int amount, int totalPrice) {
            this.book = book;
            this.amount = amount;
            this.totalPrice = totalPrice;
        }
    }


    @Data
    public static class OrderDetailResponse {
        private List<OrderDetailDTO> orders;
        private int totalOrderPrice;

        public OrderDetailResponse(List<OrderDetailDTO> orders, int totalOrderPrice) {
            this.orders = orders;
            this.totalOrderPrice = totalOrderPrice;
        }
    }


    @Data
    public static class OrderRegisterDTO {
        private Long userId;
        private List<BookOrderRegisterDTO> books;
        private Address deliveryAddress;
    }


    @Data
    public static class OrderHistoryListDTO {
        private Long orderId;
        private int totalOrderPrice;
        private int totalAmount;
        private OrderStatus status;
        private LocalDateTime orderDate;

        private int orderBookCount;
        private String orderBookName;

        public OrderHistoryListDTO(Order order, List<OrderBook> orderBooks, String orderBookName) {
            this.orderId = order.getId();
            this.totalOrderPrice = orderBooks.stream().collect(reducing(0, ob -> ob.getOrderPrice() * ob.getAmount(), Integer::sum));
            this.totalAmount = orderBooks.stream().collect(reducing(0, OrderBook::getAmount, Integer::sum));
            this.status = order.getStatus();
            this.orderDate = order.getRegDate();

            this.orderBookCount = orderBooks.size();
            this.orderBookName = orderBookName;
        }
    }


    @Data
    @AllArgsConstructor
    public static class OrderHistoryListResponse {
        private List<OrderHistoryListDTO> orders;
        private PageResultDTO<OrderHistoryListDTO> pageResult;
    }


    @Data
    public static class OrderHistoryDetailDTO {
        private String bookName;
        private int amount;
        private int price;

        public OrderHistoryDetailDTO(OrderBook orderBook) {
            this.amount = orderBook.getAmount();
            this.price = orderBook.getOrderPrice();
            this.bookName = orderBook.getBook().getName();
        }
    }

    @Data
    public static class OrderHistoryDetailResponse {
        private List<OrderHistoryDetailDTO> orders;
        private Long orderId;
        private String orderer;
        private LocalDateTime orderDate;
        private OrderStatus status;
        private int totalOrderPrice;
        private Address deliveryAddr;


        public OrderHistoryDetailResponse(Order order) {
            this.orderId = order.getId();
            this.orderer = order.getOrderer().getName(); // 지연 로딩
            this.orderDate = order.getRegDate();
            this.status = order.getStatus();
            this.deliveryAddr = order.getDeliveryAddress();

            List<OrderBook> orderBooks = order.getOrderBooks();
            this.totalOrderPrice = orderBooks.stream().collect(reducing(0, ob -> ob.getOrderPrice() * ob.getAmount(), Integer::sum));
            this.orders = orderBooks.stream().map(ob -> new OrderHistoryDetailDTO(ob)).collect(Collectors.toList());
        }
    }
}
