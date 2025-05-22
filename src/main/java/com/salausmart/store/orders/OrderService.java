package com.salausmart.store.orders;

import com.salausmart.store.auth.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class OrderService {

    private final AuthService authService;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

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
