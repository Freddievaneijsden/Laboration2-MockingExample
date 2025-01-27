package com.example.uppgift2;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {

    List<Product> products = new ArrayList<>();

    public void addProduct(Product product) {
        if (products.contains(product)) {
            product.setQuantity(product.getQuantity() + 1);
        }
        else products.add(product);
    }

    public void removeProduct(Product product) {
        if (products.contains(product) && product.getQuantity() > 1) {
            product.setQuantity(product.getQuantity() - 1);
        }
        else products.remove(product);
    }

    public int getTotalPrice() {
        int total = 0;
        for (Product product : products) {
            total += product.getPrice();
        }
        return total;
    }

    public void showProductsInShoppingCart() {
        for (Product product : products) {
            System.out.println(product);
        }
    }
}
