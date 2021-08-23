package com.prac.service;

import com.prac.domain.Book;
import com.prac.domain.Order;
import com.prac.domain.Order.OrderInfoVO;
import com.prac.domain.User;
import com.prac.dto.CartDTO;
import com.prac.dto.CartDTO.CartInfoDTO;
import com.prac.dto.OrderDTO;
import com.prac.dto.OrderDTO.*;
import com.prac.dto.PageResultDTO;
import com.prac.repository.book.BookRepository;
import com.prac.repository.order.OrderRepository;
import com.prac.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;


    @Transactional(readOnly = true)
    public OrderDetailResponse getOrderDetailResponse(List<CartInfoDTO> cartDTOs) {
        return orderRepository.getOrderDetailResponse(cartDTOs);
    }


    public void createOrder(OrderRegisterDTO orderRegisterDTO) {
        User user = userRepository.findById(orderRegisterDTO.getUserId()).orElseThrow(() -> new EntityNotFoundException());

        List<Long> bookIds = orderRegisterDTO.getBooks().stream().map(dto -> dto.getBookId()).collect(Collectors.toList());
        List<Book> books = bookRepository.findAllById(bookIds);
        Map<Long, Integer> amountMap = orderRegisterDTO.getBooks().stream().collect(Collectors.toMap(b -> b.getBookId(), b -> b.getAmount()));

        List<OrderInfoVO> orderInfos = books.stream().map(b -> new OrderInfoVO(b, amountMap.get(b.getId()))).collect(Collectors.toList());

        Order order = Order.createOrder(user, orderInfos, orderRegisterDTO.getDeliveryAddress());
        orderRepository.save(order);
    }

    public void cancelOrder(Long orderId) {
        Order order = orderRepository.getOrderForCancel(orderId);
        order.cancel();
    }


    public OrderHistoryListResponse findOrderHistoryListResponse(Long userId, Pageable pageable, String orderBy, String status) {
        return orderRepository.findOrderHistoryListResponse(userId, pageable, orderBy, status);
    }

    public OrderHistoryDetailResponse findOrderHistoryDetailResponse(Long orderId) {
        return orderRepository.findOrderHistoryDetailResponse(orderId);
    }
}

