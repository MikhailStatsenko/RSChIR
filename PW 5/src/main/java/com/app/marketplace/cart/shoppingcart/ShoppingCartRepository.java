package com.app.marketplace.cart.shoppingcart;

import com.app.marketplace.client.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    Optional<ShoppingCart> findByClient(Client client);
}
