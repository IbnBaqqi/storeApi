package com.salausmart.store.controllers;

import com.salausmart.store.dtos.ProductDto;
import com.salausmart.store.entities.Product;
import com.salausmart.store.mappers.ProductMapper;
import com.salausmart.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@AllArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @RequestMapping
    public List<ProductDto> getAllProduct(@RequestParam(required = false, name = "categoryId") Byte categoryId) {
        List<Product> products;
        if (categoryId != null)
            products = productRepository.findByCategoryId(categoryId);
        else
            products = productRepository.findAllWithCategory(); //same as findAll but with EntityGraph(categories i.e left join)
        return products.stream().map(productMapper::productToProductDto)
//                .map(product -> new ProductDto(product.getId(), product.getName(), product.getDescription(), product.getPrice(), product.getCategory().getId()))
                .toList();
    }

    @RequestMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable  Long id) {
        var product = productRepository.findById(id).orElse(null);
        if (product == null)
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok(productMapper.productToProductDto(product));

    }
}
