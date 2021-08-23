package com.prac.repository.order;

import com.prac.domain.Order;
import com.prac.domain.QBook;
import com.prac.domain.QOrder;
import com.prac.domain.QUser;
import com.prac.domain.etc.OrderStatus;
import com.prac.domain.joinTable.OrderBook;
import com.prac.domain.joinTable.QOrderBook;
import com.prac.dto.BookDTO;
import com.prac.dto.BookDTO.BookOrderDTO;
import com.prac.dto.CartDTO;
import com.prac.dto.CartDTO.CartInfoDTO;
import com.prac.dto.OrderDTO;
import com.prac.dto.OrderDTO.*;
import com.prac.dto.PageResultDTO;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.prac.domain.QBook.book;
import static com.prac.domain.QOrder.order;
import static com.prac.domain.QUser.user;
import static com.prac.domain.joinTable.QOrderBook.orderBook;
import static java.util.stream.Collectors.reducing;

@Transactional
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    public OrderDetailResponse getOrderDetailResponse(List<CartInfoDTO> cartDTOs) {

        List<Long> bookIds = cartDTOs.stream().map(dto -> dto.getBookId()).collect(Collectors.toList());
        Map<Long, Integer> amountMap = cartDTOs.stream().collect(Collectors.toMap(dto -> dto.getBookId(), dto -> dto.getAmount()));
        Map<Long, Integer> priceSumMap = cartDTOs.stream().collect(Collectors.toMap(dto -> dto.getBookId(), dto -> dto.getPriceSum()));

        Integer totalOrderPrice = cartDTOs.stream().collect(reducing(0, CartInfoDTO::getPriceSum, Integer::sum));

        List<BookOrderDTO> bookOrderDTOList = queryFactory
                .select(Projections.constructor(BookOrderDTO.class, book))
                .from(book)
                .where(book.id.in(bookIds))
                .fetch();

        List<OrderDetailDTO> orderDetailDTOList = bookOrderDTOList.stream().map(dto -> new OrderDetailDTO(dto, amountMap.get(dto.getBookId()), priceSumMap.get(dto.getBookId()))).collect(Collectors.toList());


        return new OrderDetailResponse(orderDetailDTOList, totalOrderPrice);
    }

    @Override
    public Order getOrderForCancel(Long orderId) {
        List<OrderBook> orderBooks = queryFactory
                .selectFrom(orderBook)
                .where(orderBook.id.orderId.eq(orderId))
                .join(orderBook.book, book).fetchJoin()
                .join(orderBook.order, order).fetchJoin()
                .fetch();

        return orderBooks.get(0).getOrder();
    }

    @Override
    public OrderHistoryListResponse findOrderHistoryListResponse(Long userId, Pageable pageable, String orderBy, String status) {
        QueryResults<Order> orderResults = queryFactory
                .selectFrom(order)
                .where(order.orderer.id.eq(userId).and(orderStatusPred(status)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(orderSpecifier(orderBy))
                .fetchResults();

        List<Order> orders = orderResults.getResults();
        List<Long> orderIds = orders.stream().map(o -> o.getId()).collect(Collectors.toList());


        Map<Long, List<OrderBook>> orderBookMap = queryFactory
                .selectFrom(orderBook)
                .join(orderBook.book, book).fetchJoin()
                .where(orderBook.order.id.in(orderIds))
                .fetch()
                .stream().collect(Collectors.groupingBy(ob -> ob.getId().getOrderId()));


        List<OrderHistoryListDTO> orderHistoryListDTOS = new ArrayList<>();

        for (Order o : orders) {
            List<OrderBook> orderBooks = orderBookMap.get(o.getId());
            String bookName = orderBooks.get(0).getBook().getName();
            orderHistoryListDTOS.add(new OrderHistoryListDTO(o, orderBooks, bookName));
        }


        PageImpl<OrderHistoryListDTO> pageResult = new PageImpl<>(orderHistoryListDTOS, pageable, orderResults.getTotal());
        return new OrderHistoryListResponse(orderHistoryListDTOS, new PageResultDTO<>(pageResult));
    }


    private Predicate orderStatusPred(String status) {
        if (!StringUtils.hasText(status)) return null;

        switch (status) {
            case "order":
                return orderStatusEqOrder();

            case "cancel":
                return orderStatusEqCancel();
        }

        return null;
    }


    private Predicate orderStatusEqOrder() {
        return order.status.eq(OrderStatus.ORDER);
    }

    private Predicate orderStatusEqCancel() {
        return order.status.eq(OrderStatus.CANCEL);
    }


    private OrderSpecifier<?> orderSpecifier(String orderBy) {
        if (!StringUtils.hasText(orderBy)) {
            return orderLatest();
        }

        switch (orderBy) {
            case "latest":
                return orderLatest();

            case "past":
                return orderPast();

            default:
                return orderLatest();
        }
    }

    private OrderSpecifier<?> orderLatest() {
        return order.regDate.desc();
    }

    private OrderSpecifier<?> orderPast() {
        return order.regDate.asc();
    }



    @Override
    public OrderHistoryDetailResponse findOrderHistoryDetailResponse(Long orderId) {

        Order orderEntity = queryFactory
                .selectFrom(order).distinct()
                .join(order.orderer, user).fetchJoin()
                .join(order.orderBooks, orderBook).fetchJoin()
                .join(orderBook.book, book).fetchJoin()
                .where(order.id.eq(orderId))
                .fetchOne();

        return new OrderHistoryDetailResponse(orderEntity);
    }





}
