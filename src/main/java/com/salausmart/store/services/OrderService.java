package com.salausmart.store.services;

import com.salausmart.store.dtos.OrderDto;
import com.salausmart.store.mappers.OrderMapper;
import com.salausmart.store.repositories.OrderRepository;
import lombok.AllArgsConstructor;
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
        var orders = orderRepository.getAllByCustomer(user);
        return orders.stream().map(orderMapper::toDto).toList();
    }
}
