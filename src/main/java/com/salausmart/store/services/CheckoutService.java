package com.salausmart.store.services;

import com.salausmart.store.dtos.CheckoutRequest;
import com.salausmart.store.dtos.CheckoutResponse;
import com.salausmart.store.entities.Order;
import com.salausmart.store.exceptions.CartEmptyException;
import com.salausmart.store.exceptions.CartNotFoundException;
import com.salausmart.store.repositories.CartRepository;
import com.salausmart.store.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CheckoutService {
    private CartRepository cartRepository;
    private OrderRepository orderRepository;
    private AuthService authService;
    private CartService cartService;

    public CheckoutResponse checkout(CheckoutRequest request) {
        var cart = cartRepository.getCartWithItems(request.getCartId()).orElse(null);
        if (cart == null)
            throw new CartNotFoundException();
        if (cart.isEmpty())
            throw new CartEmptyException();

        var order = Order.fromCart(cart, authService.getCurrentUser());

        orderRepository.save(order);
        cartService.clearCart(cart.getId());

        return new CheckoutResponse(order.getId());
    }
}
