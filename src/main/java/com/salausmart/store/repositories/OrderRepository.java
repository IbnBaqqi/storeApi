package com.salausmart.store.repositories;

import com.salausmart.store.entities.Order;
import com.salausmart.store.entities.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @EntityGraph(attributePaths = "items.product")
    @Query("select o from Order o where o.customer.id = ?1")
    List<Order> getOrdersWithItemsByCustomerId(Long customerId);

    @EntityGraph(attributePaths = "items.product")
    @Query("select o from Order o where o.customer = ?1")
    List<Order> getAllByCustomer(User customer);
}