package com.salausmart.store.services;

import com.salausmart.store.dtos.OrderDto;
import com.salausmart.store.exceptions.OrderNotFoundException;
import com.salausmart.store.mappers.OrderMapper;
import com.salausmart.store.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class OrderService {

    private AuthService authService;
    private OrderRepository orderRepository;
    private OrderMapper orderMapper;

    public List<OrderDto> getAllOrder() {
        var user = authService.getCurrentUser();
        var orders = orderRepository.getOrdersByCustomer(user);
        return orders.stream().map(orderMapper::toDto).toList();
    }

    public OrderDto getOrder(Long orderId) {
        var user = authService.getCurrentUser();
        var order = orderRepository.getOrderWithItems(orderId).orElseThrow(() -> new OrderNotFoundException());
        if (!order.isPlacedBy(user))
            throw new AccessDeniedException("You don't have access to this order.");

        return orderMapper.toDto(order);
    }
}
