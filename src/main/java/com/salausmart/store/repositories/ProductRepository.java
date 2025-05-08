package com.salausmart.store.repositories;

import com.salausmart.store.entities.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @EntityGraph(attributePaths = "category")
    List<Product> findByCategoryId(Byte categoryId);

//    @Query(value = "SELECT * FROM products p LEFT JOIN categories c ON p.category_id = c.id", nativeQuery = true)
//    needs to select each column(later bro), will just use jpql for now

    @EntityGraph(attributePaths = "category")
    @Query("SELECT p from Product p")
    List<Product> findAllWithCategory();
}