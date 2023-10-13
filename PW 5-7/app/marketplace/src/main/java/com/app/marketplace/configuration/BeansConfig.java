package com.app.marketplace.configuration;

import com.app.marketplace.cart.cartitem.CartItem;
import com.app.marketplace.client.Client;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeansConfig {
    @Bean
    public Gson gson() {
        return new GsonBuilder()
                .registerTypeAdapter(CartItem.class, (JsonSerializer<CartItem>) (cartItem, typeOfSrc, context) -> {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("id", cartItem.getId());
                    jsonObject.addProperty("quantity", cartItem.getQuantity());
                    jsonObject.add("product", context.serialize(cartItem.getProduct()));

                    return jsonObject;
                })
                .registerTypeAdapter(Client.class, (JsonSerializer<Client>) (client, typeOfSrc, context) -> {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("id", client.getId());
                    jsonObject.addProperty("name", client.getName());
                    jsonObject.addProperty("email", client.getEmail());
                    jsonObject.addProperty("login", client.getLogin());
                    jsonObject.addProperty("password", client.getPassword());
                    jsonObject.addProperty("items in cart",
                            client.getShoppingCart() == null ? 0 : client.getShoppingCart().getCartItems().size());

                    return jsonObject;
                })
                .create();
    }
}

