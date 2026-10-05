package com.Mapper;

import com.dto.ProductDto;
import com.model.Product;

public class ProductMapperToDto {
    public static ProductDto mapProductToDto(Product product){
        return new ProductDto(
                product.getName(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getCategory().getName(),
                product.getVendor().getName()
        );
    }
}
