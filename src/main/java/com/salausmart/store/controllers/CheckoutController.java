package com.salausmart.store.controllers;

import com.salausmart.store.dtos.CheckoutRequest;
import com.salausmart.store.dtos.CheckoutResponse;
import com.salausmart.store.entities.Order;
import com.salausmart.store.entities.OrderItem;
import com.salausmart.store.entities.OrderStatus;
import com.salausmart.store.repositories.CartRepository;
import com.salausmart.store.repositories.OrderRepository;
import com.salausmart.store.services.AuthService;
import com.salausmart.store.services.CartService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/checkout")
public class CheckoutController {

    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final CartService cartService;
    private final AuthService authService;

    @PostMapping
    public ResponseEntity<?> checkout(@Valid @RequestBody CheckoutRequest request) {
        var cart = cartRepository.getCartWithItems(request.getCartId()).orElse(null);
        if (cart == null)
            return ResponseEntity.badRequest().body(Map.of("error","Cart not found"));
        if (cart.getItems().isEmpty())
            return ResponseEntity.badRequest().body(Map.of("error","Cart is empty"));

        var order = new Order();
        order.setTotalPrice(cart.getCartTotalPrice());
        order.setStatus(OrderStatus.PENDING);
        order.setCustomer(authService.getCurrentUser());

        cart.getItems().forEach(item -> {
            var orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(item.getProduct());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setTotalPrice(item.getToTalPrice());
            orderItem.setUnitPrice(item.getProduct().getPrice());
            order.getItems().add(orderItem);
        });

        orderRepository.save(order);
        cartService.clearCart(cart.getId());

        return ResponseEntity.ok(new CheckoutResponse(order.getId()));
    }

}
