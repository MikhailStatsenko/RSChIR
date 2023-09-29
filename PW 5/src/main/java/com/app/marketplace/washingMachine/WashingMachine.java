package com.app.marketplace.washingMachine;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("washing_machines")
public class WashingMachine {
    @Id
    private Long id;
    private String manufacturer;
    private int tankVolume;
    private String sellerNumber;
    private String productType;
    private double price;
    private String title;
}
