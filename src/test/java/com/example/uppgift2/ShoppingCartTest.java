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
        assertThat(shoppingCart.addProduct("Milk").equals("Milk")).isTrue();
    }

}
