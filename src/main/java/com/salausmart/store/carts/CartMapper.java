package com.salausmart.store.carts;

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
