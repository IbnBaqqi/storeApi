package com.salausmart.store.dtos;

import com.salausmart.store.entities.CartItem;
import lombok.Data;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
public class CartDto {

    private UUID id;
    private Set<CartItem> items = new HashSet<>();
    private BigDecimal totalPrice = BigDecimal.ZERO;
}
