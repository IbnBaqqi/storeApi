package com.salausmart.store.mappers;

import com.salausmart.store.dtos.CartDto;
import com.salausmart.store.dtos.CartItemDto;
import com.salausmart.store.entities.Cart;
import com.salausmart.store.entities.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface CartMapper {
    @Mapping(target = "totalPrice", expression = "java(cart.getCartTotalPrice())")
    @Mapping(target = "items", source = "items")
    CartDto toCartDto(Cart cart);

    @Mapping(target = "totalPrice", expression = "java(cartItem.getToTalPrice())")
    CartItemDto ToCartItemDto(CartItem cartItem);

}
