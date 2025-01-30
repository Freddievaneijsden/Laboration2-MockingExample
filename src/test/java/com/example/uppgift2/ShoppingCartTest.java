package com.example.uppgift2;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ShoppingCartTest {
    ShoppingCart shoppingCart = new ShoppingCart();
    Product milk = new Product("Milk", 20);
    Product butter = new Product("Butter", 40);
    Product cheese = new Product("Cheese", 80);

    /*
    What operation are we testing?
    Under what circumstances?
    What is the expected result?
    UnitOfWork_StateUnderTest_ExpectedBehavior
     */


    //Lägga till varor
    //Ta bort varor
    //Beräkna totalpris
    //Applicera rabatter
    //Hantera kvantitetsuppdateringar

    @Test
    void shoppingCartShouldNotBeNull() {
        assertThat(shoppingCart).isNotNull();
    }

    @Test
    void addProductSavesProductToShoppingCart() {
        shoppingCart.addProduct(milk);

        assertThat(shoppingCart.products.getFirst().getName()).isEqualTo("Milk");
    }

    @Test
    void addProductsSavesProductsToShoppingCart() {
        shoppingCart.addProduct(milk);
        shoppingCart.addProduct(butter);

        assertThat(shoppingCart.products.get(0).getName()).isEqualTo("Milk");
        assertThat(shoppingCart.products.get(1).getName()).isEqualTo("Butter");
    }

    @Test
    void removeProductRemovesProductFromShoppingCart() {
        shoppingCart.addProduct(milk);
        shoppingCart.removeProduct(milk);

        assertThat(shoppingCart.products.isEmpty()).isTrue();
    }

    @Test
    void removeProductThatDoesNotExistInShoppingCartThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            shoppingCart.removeProduct(milk);
        });
        assertThat(exception.getMessage()).isEqualTo("Product not in shopping cart");
    }

    @Test
    void getTotalPriceForProductsInShoppingCartReturnsTotalPrice() {
        shoppingCart.addProduct(milk);
        shoppingCart.addProduct(butter);

        assertThat(shoppingCart.getTotalPrice()).isEqualTo(60);
    }

    @Test
    void addDiscountToAllProductsInShoppingCartReturnsTotalPriceWithDiscount() {
        shoppingCart.addProduct(milk);
        shoppingCart.addProduct(butter);

        assertThat(DiscountService.addDiscountToAllProducts(shoppingCart, 20)).isEqualTo(48);
    }

    @Test
    void addDiscountToProductInShoppingCartReturnsSinglePriceWithDiscount() {
        shoppingCart.addProduct(milk);
        DiscountService.addDiscountToProduct(milk, 10);

        assertThat(milk.getPrice()).isEqualTo(18);
    }

    @Test
    void addProductOfSameTypeIncreaseQuantityByOneInShoppingCart() {
        shoppingCart.addProduct(milk);
        shoppingCart.addProduct(milk);

        assertThat(shoppingCart.products.getFirst().getQuantity()).isEqualTo(2);
    }

    @Test
    void removeProductOfSameTypeDecreasesQuantityByOneInShoppingCart() {
        shoppingCart.addProduct(milk);
        shoppingCart.addProduct(milk);
        shoppingCart.removeProduct(milk);

        assertThat(shoppingCart.products.getFirst().getQuantity()).isEqualTo(1);
    }

    @Test
    void findProductByNameReturnsProductFromShoppingCart() {
        shoppingCart.addProduct(milk);
        shoppingCart.addProduct(butter);
        shoppingCart.addProduct(cheese);

        assertThat(shoppingCart.findProductByName("Cheese")).get().isEqualTo(cheese);
    }

    @Test
    void findProductByNameReturnsProductWhenNameIsPartiallyMatched() {
        shoppingCart.addProduct(milk);
        shoppingCart.addProduct(butter);

        assertThat(shoppingCart.findProductByName("mi")).get().isEqualTo(milk);
        
    }

    @Test
    void returnOptionalEmptyIfProductNameDoesNotExistInShoppingCart() {
        shoppingCart.addProduct(milk);
        shoppingCart.addProduct(butter);

        assertThat(shoppingCart.findProductByName("Cheese")).isNotPresent();
    }

    @Test
    void creatingProductWithNegativePriceShouldThrowException() {
        Exception exception = assertThrows(Exception.class, () -> {
            // Code that should throw the exception
            new Product("Tomato", -20);
        });
        assertThat(exception.getMessage()).isEqualTo("Price cannot be negative");
    }

    @Test
    void addDiscountGreaterThen100ToShoppingCartShouldThrowException() {
        shoppingCart.addProduct(milk);
        shoppingCart.addProduct(butter);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            DiscountService.addDiscountToAllProducts(shoppingCart, 101);
        });

        assertThat(exception.getMessage()).isEqualTo("Invalid discount, please enter a number between 0 and 100");
    }

    @Test
    void addDiscountToAllProductsWhen100ShouldReturn0() {
        shoppingCart.addProduct(milk);
        shoppingCart.addProduct(butter);

        assertThat(DiscountService.addDiscountToAllProducts(shoppingCart, 100)).isEqualTo(0);
    }

    @Test
    void addDiscountToAllProductsLessThen0ShouldThrowException() {
        shoppingCart.addProduct(milk);
        shoppingCart.addProduct(butter);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            DiscountService.addDiscountToAllProducts(shoppingCart, -1);
        });

        assertThat(exception.getMessage()).isEqualTo("Invalid discount, please enter a number between 0 and 100");
    }

    @Test
    void getTotalPriceWhenShoppingCartIsEmptyShouldReturn0() {
        assertThat(shoppingCart.getTotalPrice()).isEqualTo(0);
    }

    @Test
    void addProductNullToShoppingCartShouldThrowException() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            shoppingCart.addProduct(null);
        });
        assertThat(exception.getMessage()).isEqualTo("Product cannot be null");
    }

}
