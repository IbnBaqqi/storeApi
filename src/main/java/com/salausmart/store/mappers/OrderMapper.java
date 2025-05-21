package com.salausmart.store.mappers;

import com.salausmart.store.dtos.*;
import com.salausmart.store.entities.Order;
import com.salausmart.store.entities.OrderItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderDto toDto(Order order);
    OrderItemDto toDto(OrderItem orderItem);

}
