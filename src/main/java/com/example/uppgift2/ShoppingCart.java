package com.example.uppgift2;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ShoppingCart {

    List<Product> products = new ArrayList<>();

    public void addProduct(Product product) {
        if (product == null) {
            throw new NullPointerException("Product cannot be null");
        }
        else {
            if (products.contains(product)) {
                product.setQuantity(product.getQuantity() + 1);
            } else products.add(product);
        }
    }

    public void removeProduct(Product product) {
        if (products.contains(product)) {
        if (product.getQuantity() > 1) {
            product.setQuantity(product.getQuantity() - 1);
        }
        else products.remove(product);
    }
        else throw new IllegalArgumentException("Product not in shopping cart");
    }

    public int getTotalPrice() {
        int total = 0;
        for (Product product : products) {
            total += product.getPrice();
        }
        return total;
    }


    public Optional<Product> findProductByName(String productName) {
        for (Product product : products) {
            if (product.getName().toLowerCase().contains(productName.trim().toLowerCase())) {
                return Optional.of(product);
            }
        }
        return Optional.empty();
    }
}
