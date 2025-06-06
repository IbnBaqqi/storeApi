package com.salausmart.store.products;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(source = "category.id", target = "categoryId")
    ProductDto productToProductDto(Product product);

//    @Mapping(source = "categoryId", target = "category.id") not going to work)
//    got detach added to persist error
    Product toEntity(ProductDto productDto);

    @Mapping(target = "id", ignore = true)
    //@Mapping(source = "categoryId", target = "category.id")
    void update(ProductDto productDto, @MappingTarget Product product);
}
