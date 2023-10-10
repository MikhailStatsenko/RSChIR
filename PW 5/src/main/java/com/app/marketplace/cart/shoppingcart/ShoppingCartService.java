package com.app.marketplace.cart.shoppingcart;

import com.app.marketplace.cart.cartitem.CartItem;
import com.app.marketplace.client.Client;
import com.app.marketplace.product.Product;
import com.app.marketplace.product.ProductService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShoppingCartService {
    private final ProductService productService;
    private final ShoppingCartRepository shoppingCartRepository;

    @Autowired
    public ShoppingCartService(ProductService productService, ShoppingCartRepository shoppingCartRepository) {
        this.productService = productService;
        this.shoppingCartRepository = shoppingCartRepository;
    }

    public void createShoppingCart(Client client) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setClient(client);
        shoppingCartRepository.save(shoppingCart);
    }

    public Optional<ShoppingCart> getShoppingCartByClient(Client client) {
        return shoppingCartRepository.findByClient(client);
    }

    @Transactional
    public Optional<List<CartItem>> order(ShoppingCart shoppingCart) {
        List<CartItem> itemsToOrder =  shoppingCart.getCartItems();

        boolean enoughGoods = itemsToOrder.stream()
                .allMatch(item -> item.getQuantity() <= item.getProduct().getQuantity());

        if (!enoughGoods)
            return Optional.empty();

        for (CartItem item : itemsToOrder) {
            Product product = item.getProduct();
            productService.updateQuantity(product, product.getQuantity() - item.getQuantity());
        }
        shoppingCartRepository.delete(shoppingCart);

        return Optional.of(itemsToOrder);
    }
}

