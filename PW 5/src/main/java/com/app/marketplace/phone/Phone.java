package com.app.marketplace.phone;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("phones")
public class Phone {
    @Id
    private Long id;
    private String manufacturer;
    private int batteryCapacity;
    private String sellerNumber;
    private String productType;
    private double price;
    private String title;
}

