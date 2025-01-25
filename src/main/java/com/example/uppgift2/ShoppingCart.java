package com.example.uppgift2;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {

    List<Product> products = new ArrayList<>();

    public void addProduct(Product productName) {
        products.add(productName);
    }

    public void removeProduct(Product productName) {
        products.remove(productName);
    }

    public int getTotalPrice() {
        int total = 0;
        for (Product product : products) {
            total += product.getPrice();
        }
        return total;
    }
}
