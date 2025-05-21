package com.salausmart.store.dtos;

import com.salausmart.store.entities.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrderDto {

    private Long id;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private List<OrderItemDto> items = new ArrayList<>();
    private BigDecimal totalPrice;
}
