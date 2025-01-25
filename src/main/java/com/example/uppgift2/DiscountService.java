package com.example.uppgift2;


public class DiscountService {

    public static double addDiscountToAllProducts(ShoppingCart shoppingCart, int discount) {
        double totalDiscount = 0;
        for (Product product : shoppingCart.products) {
            totalDiscount += product.getPrice() * 0.2;
        }
        return shoppingCart.getTotalPrice() - totalDiscount;
    }

    public static void addDiscountToProduct(Product product, double discount) {
        double totalDiscount = product.getPrice() * (discount/100);
        double newPrice = product.getPrice() - totalDiscount;

        product.setPrice(newPrice);
    }
}
