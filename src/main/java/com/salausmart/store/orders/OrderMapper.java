package com.salausmart.store.orders;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderDto toDto(Order order);
    OrderItemDto toDto(OrderItem orderItem);

}
