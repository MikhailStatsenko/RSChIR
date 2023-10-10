package com.app.marketplace.client;

import com.app.marketplace.cart.shoppingcart.ShoppingCart;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String login;
    private String password;

    @OneToOne(mappedBy = "client")
    private ShoppingCart shoppingCart;
}

