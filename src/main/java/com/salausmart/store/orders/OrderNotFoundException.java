package com.salausmart.store.orders;

public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException() {
        super("Order not found");
    }
}
