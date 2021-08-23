package com.prac.api;

import com.prac.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
@Slf4j
public class OrderApi {

    private final OrderService orderService;

    @PatchMapping("/{id}")
    public void cancelOrder(@PathVariable("id") Long orderId) {
        log.info("orderId: " + orderId);
        orderService.cancelOrder(orderId);
    }
}
