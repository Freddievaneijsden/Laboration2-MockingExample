package com.example.uppgift2;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ShoppingCartTest {

    //Lägga till varor
    //Ta bort varor
    //Beräkna totalpris
    //Applicera rabatter
    //Hantera kvantitetsuppdateringar

    @Test
    void createShoppingCart() {
        ShoppingCart shoppingCart = new ShoppingCart();
    }

    @Test
    void addProductToShoppingCart() {
        ShoppingCart shoppingCart = new ShoppingCart();
        Product milk = new Product("Milk");
        shoppingCart.addProduct(milk);
        assertThat(shoppingCart.products.get(0)).isEqualTo(milk);
    }

    @Test
    void addTwoProductsToShoppingCart() {
        ShoppingCart shoppingCart = new ShoppingCart();
        Product milk = new Product("Milk");
        Product butter = new Product("Butter");
        shoppingCart.addProduct(milk);
        shoppingCart.addProduct(butter);
        assertThat(shoppingCart.products.get(0)).isEqualTo(milk);
        assertThat(shoppingCart.products.get(1)).isEqualTo(butter);
    }

}
