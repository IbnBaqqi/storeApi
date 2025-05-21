package com.salausmart.store.controllers;

import com.salausmart.store.dtos.OrderDto;
import com.salausmart.store.mappers.OrderMapper;
import com.salausmart.store.repositories.OrderRepository;
import com.salausmart.store.services.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final AuthService authService;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @GetMapping
    public ResponseEntity<?> getOrder() {
        var user = authService.getCurrentUser();
        var order = orderRepository.getOrdersWithItemsByCustomerId(user.getId());
        List<OrderDto> orderDtos = new ArrayList<>();

        order.forEach(order1 -> {
            orderDtos.add(orderMapper.toDto(order1));
        });
        return ResponseEntity.ok(orderDtos);
    }
}
