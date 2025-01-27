package com.example.uppgift2;


public class DiscountService {

    public static double addDiscountToAllProducts(ShoppingCart shoppingCart, double discount) {
        double totalDiscount = 0;
        if (discount <= 100) {
        for (Product product : shoppingCart.products) {
            totalDiscount += product.getPrice() * (discount / 100);
        }
        return shoppingCart.getTotalPrice() - totalDiscount;
        }
        else throw new IllegalArgumentException("Discount limit exceeded, please try again");
    }

    public static void addDiscountToProduct(Product product, double discount) {
        if (discount <= 100) {
            double totalDiscount = product.getPrice() * (discount / 100);
            double newPrice = product.getPrice() - totalDiscount;

            product.setPrice(newPrice);
        }
        else throw new IllegalArgumentException("Discount limit exceeded, please try again");
    }
}
