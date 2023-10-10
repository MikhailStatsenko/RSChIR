package com.app.marketplace.product;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "product_type")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int quantity;
    private String sellerNumber;
    private double price;
    private String title;
}

