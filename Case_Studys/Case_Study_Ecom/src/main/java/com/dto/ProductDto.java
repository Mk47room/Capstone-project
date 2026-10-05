package com.dto;

public record ProductDto(
        String name,
        double price,
        int stockQuantity,
        String categoryName,
        String vendorName
) {
}
