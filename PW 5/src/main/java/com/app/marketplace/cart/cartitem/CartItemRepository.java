package com.app.marketplace.cart.cartitem;

import com.app.marketplace.cart.shoppingcart.ShoppingCart;
import com.app.marketplace.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByShoppingCartAndProduct(ShoppingCart shoppingCart, Product product);
}

