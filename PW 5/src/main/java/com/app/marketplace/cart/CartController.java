package com.app.marketplace.cart;

import com.app.marketplace.cart.cartitem.CartItem;
import com.app.marketplace.cart.cartitem.CartItemService;
import com.app.marketplace.cart.shoppingcart.ShoppingCart;
import com.app.marketplace.cart.shoppingcart.ShoppingCartService;
import com.app.marketplace.cart.util.AddToCartDTO;
import com.app.marketplace.cart.util.UpdateQuantityDTO;
import com.app.marketplace.client.ClientService;
import com.app.marketplace.product.ProductService;
import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/marketplace/cart")
public class CartController {
    private final Gson gson;
    private final ClientService clientService;
    private final ProductService productService;
    private final CartItemService cartItemService;
    private final ShoppingCartService shoppingCartService;

    public CartController(Gson gson, ClientService clientService,
                          ProductService productService, CartItemService cartItemService,
                          ShoppingCartService shoppingCartService) {
        this.gson = gson;
        this.clientService = clientService;
        this.productService = productService;
        this.cartItemService = cartItemService;
        this.shoppingCartService = shoppingCartService;
    }

    @GetMapping("/{clientId}")
    public ResponseEntity<?> getCart(@PathVariable Long clientId) {
        return clientService.getClientById(clientId)
                .flatMap(shoppingCartService::getShoppingCartByClient)
                .map(cart -> ResponseEntity.ok().body(gson.toJson(cart.getCartItems())))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Client does not exist"));
    }

    @PostMapping("/make-order/{clientId}")
    public ResponseEntity<?> makeOrder(@PathVariable Long clientId) {
        return clientService.getClientById(clientId)
                .map(client -> {
                    ShoppingCart shoppingCart = client.getShoppingCart();
                    if (shoppingCart.getCartItems().isEmpty()) {
                        return new ResponseEntity<>("Cart is empty", HttpStatus.NO_CONTENT);
                    } else {
                        Optional<List<CartItem>> orderedItems = shoppingCartService.order(shoppingCart);
                        return orderedItems.map(cartItems -> ResponseEntity.ok(gson.toJson(cartItems)))
                                .orElse(ResponseEntity.unprocessableEntity().body("Not enough product"));
                    }
                })
                .orElse(ResponseEntity.unprocessableEntity().body("Client does not exist"));
    }

    @PostMapping("/add-to-cart")
    public ResponseEntity<?> addToCart(@RequestBody AddToCartDTO info) {
        return clientService.getClientById(info.clientId())
                .flatMap(client -> productService.getProductById(info.productId())
                        .filter(product -> product.getQuantity() >= info.quantity())
                        .map(product -> {
                            CartItem addedItem = cartItemService.addToCart(client, product, info.quantity());
                            return ResponseEntity.ok(gson.toJson(addedItem));
                        })
                )
                .orElse(ResponseEntity.unprocessableEntity().body("Client or product does not exist or not enough product"));
    }

    @PatchMapping("/update_quantity")
    public ResponseEntity<?> updateQuantity(@RequestBody UpdateQuantityDTO info) {
        return cartItemService.getById(info.cartItemId())
                .filter(cartItem -> info.quantity() >= 0 && cartItem.getProduct().getQuantity() >= info.quantity())
                .map(cartItem -> {
                    CartItem updatedItem = cartItemService.updateCartItemQuantity(cartItem, info.quantity());
                    return ResponseEntity.ok(gson.toJson(updatedItem));
                })
                .orElse(ResponseEntity.unprocessableEntity().body("No such item in the cart or invalid quantity"));
    }

    @DeleteMapping("/remove-from-cart/{cartItemId}")
    public ResponseEntity<?> removeFromCart(@PathVariable Long cartItemId) {
        return cartItemService.getById(cartItemId)
                .map(cartItem -> {
                    cartItemService.removeFromCart(cartItem);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("No such item in the cart"));
    }
}


