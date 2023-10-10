package com.app.marketplace.cart.cartitem;

import com.app.marketplace.cart.shoppingcart.ShoppingCart;
import com.app.marketplace.product.Product;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Product product;

    @ManyToOne
    @JoinColumn(name = "cartItems")
    private ShoppingCart shoppingCart;

    private int quantity;

    public CartItem(Product product, ShoppingCart shoppingCart, int quantity) {
        this.product = product;
        this.shoppingCart = shoppingCart;
        this.quantity = quantity;
    }
}
