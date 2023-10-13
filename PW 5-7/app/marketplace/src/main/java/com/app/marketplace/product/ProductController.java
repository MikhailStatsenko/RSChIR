package com.app.marketplace.product;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/marketplace/products")
public class ProductController {
    private final Gson gson;
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService, Gson gson) {
        this.productService = productService;
        this.gson = gson;
    }

    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        if (products.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(gson.toJson(products));
    }
}


