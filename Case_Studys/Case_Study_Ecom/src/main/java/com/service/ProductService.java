package com.service;

import com.Exception.IdNotFoundException;
import com.dto.ProductDto;
import com.model.Product;
import com.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void insertProduct(Product product) {

        int productId = (int) (Math.random()*1000);
        int categoryId = (int) (Math.random()*1000);
        int vendorId = (int) (Math.random()*1000);

        product.setId(productId);
        product.getCategory().setId(categoryId);
        product.getVendor().setId(vendorId);

        productRepository.insertProduct(product);
    }

    public ProductDto getProductById(int i) {
        List<ProductDto> list =productRepository.getProductById(i);
        if(list == null || list.isEmpty())
            throw new IdNotFoundException("Id not found");
        return list.getFirst();
    }

    public void updateStockById(int id, int stock) {
        productRepository.updateStockById(id,stock);
    }

    public List<Map<String, Integer>> countProductsByVendor() {
        return productRepository.countProductsByVendor();
    }
}
