package com.example.uppgift2;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ShoppingCartTest {
    ShoppingCart shoppingCart = new ShoppingCart();
    Product milk = new Product("Milk", 20);
    Product butter = new Product("Butter", 40);

    //Lägga till varor
    //Ta bort varor
    //Beräkna totalpris
    //Applicera rabatter
    //Hantera kvantitetsuppdateringar

    @Test
    void createShoppingCart() {
        assertThat(shoppingCart).isNotNull();
    }

    @Test
    void addProductToShoppingCart() {
        shoppingCart.addProduct(milk);

        assertThat(shoppingCart.products.getFirst()).isEqualTo(milk);
    }

    @Test
    void addTwoProductsToShoppingCart() {
        shoppingCart.addProduct(milk);
        shoppingCart.addProduct(butter);

        assertThat(shoppingCart.products.get(0)).isEqualTo(milk);
        assertThat(shoppingCart.products.get(1)).isEqualTo(butter);
    }

    @Test
    void removeProductFromShoppingCart() {
        shoppingCart.addProduct(milk);
        shoppingCart.removeProduct(milk);

        assertThat(shoppingCart.products.isEmpty()).isTrue();

    }

    @Test
    void calculateTotalPriceForProductsInShoppingCart() {
        shoppingCart.addProduct(milk);
        shoppingCart.addProduct(butter);

        assertThat(shoppingCart.getTotalPrice()).isEqualTo(milk.getPrice() + butter.getPrice());
    }

}
