package com.app.marketplace.product;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    public Optional<Product> getProductById(long id) {
        return productRepository.findProductById(id);
    }

    public void updateQuantity(Product product, int quantity) {
        product.setQuantity(quantity);
        productRepository.save(product);
    }
}


