package com.prac.domain;

import com.prac.domain.etc.Address;
import com.prac.domain.etc.BaseEntity;
import com.prac.domain.etc.OrderStatus;
import com.prac.domain.joinTable.OrderBook;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(exclude = {"orderer", "orderBooks"})
@AttributeOverride(name = "id", column = @Column(name = "order_id"))
@Table(name = "orders")
public class Order extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_order_to_user"))
    private User orderer;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private Address deliveryAddress;

    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
    private List<OrderBook> orderBooks = new ArrayList<>();


    private Order(User orderer, OrderStatus orderStatus, Address deliveryAddress) {
        this.orderer = orderer;
        this.status = orderStatus;
        this.deliveryAddress = new Address(deliveryAddress.getPostcode(), deliveryAddress.getRoadAddr(), deliveryAddress.getJibunAddr(), deliveryAddress.getDetailAddr(), deliveryAddress.getExtraAddr());
    }


    public static Order createOrder(User orderer, List<OrderInfoVO> orderInfos, Address deliveryAddress) {
        Order order = new Order(orderer, OrderStatus.ORDER, deliveryAddress);

        for (OrderInfoVO orderInfo : orderInfos) {
            OrderBook orderBook = new OrderBook(order, orderInfo);
            Book book = orderInfo.getBook();
            book.minusStockQuantity(orderInfo.getAmount());
            order.orderBooks.add(orderBook);
        }

        return order;
    }


    public void cancel() {
        this.status = OrderStatus.CANCEL;

        for (OrderBook orderBook : this.orderBooks) {
            orderBook.cancel();
        }
    }



    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderInfoVO {
        private Book book;
        private int amount;
    }
}
