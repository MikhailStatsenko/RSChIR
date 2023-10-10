package com.app.marketplace.cart.cartitem;

import com.app.marketplace.cart.shoppingcart.ShoppingCart;
import com.app.marketplace.cart.shoppingcart.ShoppingCartService;
import com.app.marketplace.client.Client;
import com.app.marketplace.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartItemService {
    private final CartItemRepository cartItemRepository;
    private final ShoppingCartService shoppingCartService;

    @Autowired
    public CartItemService(CartItemRepository cartItemRepository, ShoppingCartService shoppingCartService) {
        this.cartItemRepository = cartItemRepository;
        this.shoppingCartService = shoppingCartService;
    }

    public Optional<CartItem> getById(Long id) {
        return cartItemRepository.findById(id);
    }

    public CartItem addToCart(Client client, Product product, int quantity) {
        Optional<ShoppingCart> shoppingCartOptional = shoppingCartService.getShoppingCartByClient(client);
        if (shoppingCartOptional.isEmpty())
            shoppingCartService.createShoppingCart(client);

        ShoppingCart shoppingCart = shoppingCartService.getShoppingCartByClient(client).get();
        CartItem existingCartItem = cartItemRepository.findByShoppingCartAndProduct(shoppingCart, product);

        if (existingCartItem != null) {
            existingCartItem.setQuantity(quantity);
            return cartItemRepository.save(existingCartItem);
        } else {
            CartItem newCartItem = new CartItem(product, shoppingCart, quantity);
            return cartItemRepository.save(newCartItem);
        }
    }

    public void removeFromCart(CartItem cartItem) {
        cartItemRepository.delete(cartItem);
    }

    public CartItem updateCartItemQuantity(CartItem cartItem, int quantity) {
        cartItem.setQuantity(quantity);
        return cartItemRepository.save(cartItem);
    }
}

