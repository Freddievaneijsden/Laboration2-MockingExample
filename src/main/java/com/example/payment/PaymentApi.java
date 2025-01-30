package com.example.payment;

public class PaymentApi implements PaymentApiResponse{
    String apiKey;
    double amount;

    public PaymentApi(String apiKey, double amount) {
        this.apiKey = apiKey;
        this.amount = amount;
    }

    public static PaymentApiResponse charge(String apiKey, double amount) {
        if (apiKey == null || amount <= 0) {
            throw new IllegalArgumentException("apiKey and amount must be greater than 0");
        }
        else
            return new PaymentApi(apiKey, amount);
    }

    @Override
    public boolean isSuccess() {
        return false;
    }
}
