package com.salausmart.store.repositories;

import com.salausmart.store.entities.Order;
import com.salausmart.store.users.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @EntityGraph(attributePaths = "items.product")
    @Query("select o from Order o where o.customer.id = ?1")
    List<Order> getOrdersWithItemsByCustomerId(Long customerId);

    @EntityGraph(attributePaths = "items.product")
    @Query("select o from Order o where o.customer = ?1")
    List<Order> getOrdersByCustomer(User customer);

    @EntityGraph(attributePaths = "items.product")
    @Query("SELECT o FROM Order o WHERE o.id = :orderId")
    Optional<Order> getOrderWithItems(@Param("orderId") Long orderId);
}