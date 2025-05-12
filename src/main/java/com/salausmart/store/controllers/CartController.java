package com.salausmart.store.controllers;

import com.salausmart.store.dtos.AddItemToCartRequest;
import com.salausmart.store.dtos.CartDto;
import com.salausmart.store.dtos.CartItemDto;
import com.salausmart.store.entities.Cart;
import com.salausmart.store.entities.CartItem;
import com.salausmart.store.mappers.CartMapper;
import com.salausmart.store.repositories.CartRepository;
import com.salausmart.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/carts")
public class CartController {

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final ProductRepository productRepository;

    @PostMapping
    public ResponseEntity<CartDto> createCart(UriComponentsBuilder uriBuilder) {
        var cart = new Cart();
        cartRepository.save(cart);
        var cartDto = cartMapper.toCartDto(cart);
        UUID cartUuid = cart.getId();
        var uri = uriBuilder.path("/carts/{cartUuid}").buildAndExpand(cartUuid).toUri();
        return ResponseEntity.created(uri).body(cartDto);
    }

    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartItemDto> addToCart(@RequestBody AddItemToCartRequest request, @PathVariable UUID cartId) {
        var cart = cartRepository.findById(cartId).orElse(null);
        System.out.println(cartId);
        if (cart == null)
            return ResponseEntity.notFound().build();
        // Check if product is available
        var product = productRepository.findById(request.getProductId()).orElse(null);
        if (product == null)
            return ResponseEntity.badRequest().build();

        var cartItem = cart.getCartItem().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst()
                .orElse(null);

        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
        } else {
            cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(1);
            cartItem.setCart(cart);
            cart.getCartItem().add(cartItem);
        }

        cartRepository.save(cart);
        var cartItemDto = cartMapper.ToCartItemDto(cartItem);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
    }
}
