package com.prac.domain.joinTable;

import com.prac.domain.Book;
import com.prac.domain.Order;
import com.prac.domain.Order.OrderInfoVO;
import com.prac.domain.etc.AuditingEntity;
import com.prac.domain.etc.BaseEntity;
import com.prac.domain.etc.OrderBookKey;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class OrderBook extends AuditingEntity {

    @EmbeddedId
    private OrderBookKey id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "fk_order_book_to_order"))
    @MapsId("orderId")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", foreignKey = @ForeignKey(name = "fk_order_book_to_book"))
    @MapsId("bookId")
    private Book book;

    private int amount;
    private int orderPrice;

    public OrderBook(Order order, OrderInfoVO orderInfo) {
        this.order = order;
        this.book = orderInfo.getBook();
        this.id = new OrderBookKey(order.getId(), orderInfo.getBook().getId());
        this.amount = orderInfo.getAmount();
        this.orderPrice = orderInfo.getBook().getPrice();
    }


    public void cancel() {
        this.book.plusStockQuantity(amount);
    }

}
