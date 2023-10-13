package com.app.marketplace.phone;

import com.app.marketplace.product.Product;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@DiscriminatorValue("Phone")
public class Phone extends Product {
    private String manufacturer;
    private int batteryCapacity;
}

