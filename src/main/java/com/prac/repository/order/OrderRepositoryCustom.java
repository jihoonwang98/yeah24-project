package com.prac.repository.order;

import com.prac.domain.Order;
import com.prac.dto.CartDTO.CartInfoDTO;
import com.prac.dto.OrderDTO;
import com.prac.dto.OrderDTO.OrderDetailResponse;
import com.prac.dto.OrderDTO.OrderHistoryDetailResponse;
import com.prac.dto.OrderDTO.OrderHistoryListResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderRepositoryCustom {

    OrderDetailResponse getOrderDetailResponse(List<CartInfoDTO> cartDTOs);

    Order getOrderForCancel(Long orderId);

    OrderHistoryListResponse findOrderHistoryListResponse(Long userId, Pageable pageable, String orderBy, String status);

    OrderHistoryDetailResponse findOrderHistoryDetailResponse(Long orderId);
}
