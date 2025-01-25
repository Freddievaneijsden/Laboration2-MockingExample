package com.example.uppgift2;


public class DiscountService {

    public static double addDiscountToProducts(ShoppingCart shoppingCart, int discount) {
        double totalDiscount = 0;
        for (Product product : shoppingCart.products) {
            totalDiscount += product.getPrice() * 0.2;
        }
        return shoppingCart.getTotalPrice() - totalDiscount;
    }
}
