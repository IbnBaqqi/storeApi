package com.salausmart.store.services;

import com.salausmart.store.entities.Order;

public interface PaymentGateway {
    CheckoutSession createCheckoutSession(Order order);
}
