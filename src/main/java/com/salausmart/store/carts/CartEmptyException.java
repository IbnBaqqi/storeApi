package com.salausmart.store.carts;

public class CartEmptyException extends RuntimeException {

    public CartEmptyException() {
        super("Cart is empty");
    }
}
