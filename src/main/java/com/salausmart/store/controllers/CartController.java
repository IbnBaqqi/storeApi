package com.salausmart.store.controllers;

import com.salausmart.store.dtos.AddItemToCartRequest;
import com.salausmart.store.dtos.CartDto;
import com.salausmart.store.dtos.CartItemDto;
import com.salausmart.store.dtos.UpdateCartItemRequest;
import com.salausmart.store.entities.Cart;
import com.salausmart.store.mappers.CartMapper;
import com.salausmart.store.repositories.CartRepository;
import com.salausmart.store.repositories.ProductRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
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
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        System.out.println(cartId);
        if (cart == null)
            return ResponseEntity.notFound().build();
        // Check if product is available
        var product = productRepository.findById(request.getProductId()).orElse(null);
        if (product == null)
            return ResponseEntity.badRequest().build();

        var cartItem = cart.addItem(product);

        cartRepository.save(cart);
        var cartItemDto = cartMapper.ToCartItemDto(cartItem);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemDto);
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<CartDto> getCart(@PathVariable UUID cartId) {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null)
            return ResponseEntity.notFound().build();

        var cartDto = cartMapper.toCartDto(cart);
        return ResponseEntity.ok(cartDto);
    }


    @PutMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> updateItem(@Valid @RequestBody UpdateCartItemRequest request, @PathVariable UUID cartId, @PathVariable Long productId) {

        // check if cart exist
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        System.out.println(cartId);
        if (cart == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error", "Cart not found.")
            );
        // Check if product is in the cart & available
        var productInCart = cart.getItem(productId);

        if (productInCart == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error", "Product was not found in the cart.")
            );

        productInCart.setQuantity(request.getQuantity());
        cartRepository.save(cart);
        var cartItemDto = cartMapper.ToCartItemDto(productInCart);
        return ResponseEntity.ok(cartItemDto);
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> deleteItem( @PathVariable UUID cartId, @PathVariable Long productId) {
        // check if cart exist
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        System.out.println(cartId);
        if (cart == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error", "Cart not found.")
            );

        cart.removeItem(productId);

        cartRepository.save(cart);

        return ResponseEntity.noContent().build();
    }
}
