package com.salausmart.store.mappers;

import com.salausmart.store.dtos.ProductDto;
import com.salausmart.store.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(source = "category.id", target = "categoryId")
    ProductDto productToProductDto(Product product);

//    @Mapping(source = "categoryId", target = "category.id") not gonna work)
//    got detach added to persist error
    Product toEntity(ProductDto productDto);
}
