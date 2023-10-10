package com.app.marketplace.washingMachine;

import com.app.marketplace.product.Product;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@DiscriminatorValue("WashingMachine")
public class WashingMachine extends Product {
    private String manufacturer;
    private int tankVolume;
}

