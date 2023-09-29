package com.app.marketplace.book;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("books")
public class Book {
    @Id
    private Long id;
    private String author;
    private String sellerNumber;
    private String productType;
    private double price;
    private String title;
}