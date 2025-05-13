package com.salausmart.store.services;

import com.salausmart.store.dtos.CartDto;
import com.salausmart.store.dtos.CartItemDto;
import com.salausmart.store.entities.Cart;
import com.salausmart.store.exceptions.CartNotFoundException;
import com.salausmart.store.exceptions.ProductNotFoundException;
import com.salausmart.store.mappers.CartMapper;
import com.salausmart.store.repositories.CartRepository;
import com.salausmart.store.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CartService {
    private CartRepository cartRepository;
    private CartMapper cartMapper;
    private ProductRepository productRepository;

    public CartDto createCart() {
        var cart = new Cart();
        cartRepository.save(cart);

        return cartMapper.toCartDto(cart);
    }

    public CartItemDto addToCart(UUID cartId, Long productId) {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null)
            throw new CartNotFoundException();
        // Check if product is available
        var product = productRepository.findById(productId).orElse(null);
        if (product == null)
            throw new ProductNotFoundException();

        var cartItem = cart.addItem(product);

        cartRepository.save(cart);
        return cartMapper.ToCartItemDto(cartItem);
    }

    public CartDto getCart(UUID cartId) {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null)
            throw new CartNotFoundException();

        return cartMapper.toCartDto(cart);
    }

    public CartItemDto updateItem(UUID cartId, Long productId, Integer quantity) {
        // check if cart exist
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null)
            throw new CartNotFoundException();

        // Check if product is in the cart & available
        var productInCart = cart.getItem(productId);

        if (productInCart == null)
            throw new ProductNotFoundException();

        productInCart.setQuantity(quantity);
        cartRepository.save(cart);
        return cartMapper.ToCartItemDto(productInCart);
    }

    public void removeItem( UUID cartId, Long productId) {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null)
            throw new CartNotFoundException();

        cart.removeItem(productId);

        cartRepository.save(cart);
    }
}
