package com.salausmart.store.controllers;

import com.salausmart.store.dtos.ProductDto;
import com.salausmart.store.entities.Product;
import com.salausmart.store.mappers.ProductMapper;
import com.salausmart.store.repositories.CategoryRepository;
import com.salausmart.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
@AllArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    @GetMapping
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

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable  Long id) {
        var product = productRepository.findById(id).orElse(null);
        if (product == null)
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok(productMapper.productToProductDto(product));

    }

    @PostMapping
    public ResponseEntity<ProductDto> addNewProduct(@RequestBody ProductDto productDto, UriComponentsBuilder uriBuilder) {
        var category = categoryRepository.findById(productDto.getCategoryId()).orElse(null);
        if (category == null)
            return ResponseEntity.notFound().build();

        var product = productMapper.toEntity(productDto);
        product.setCategory(category);
        productRepository.save(product);
        productDto.setId(product.getId());
       // var productDto = productMapper.productToProductDto(product); not needed, we can return requestbody Dto
        var uri = uriBuilder.path("/products/{id}").buildAndExpand(productDto.getId()).toUri();
        return ResponseEntity.created(uri).body(productDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable Long id, @RequestBody ProductDto productDto) {
        var product = productRepository.findById(id).orElse(null);
        if (product == null)
            return ResponseEntity.notFound().build();
        var category = categoryRepository.findById(productDto.getCategoryId()).orElse(null);
        if (category == null)
            return ResponseEntity.notFound().build();
        product.setCategory(category);
        productMapper.update(productDto, product);
        productRepository.save(product);
        productDto.setId(product.getId());
        return ResponseEntity.ok(productDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        var product = productRepository.findById(id).orElse(null);
        if (product == null)
            return ResponseEntity.notFound().build();
        productRepository.delete(product);
        return ResponseEntity.noContent().build();
    }
}
