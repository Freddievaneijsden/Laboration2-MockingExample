package com.example.payment;

public class PaymentApiMock implements PaymentApi, PaymentApiResponse {
    String apiKey;
    double amount;

    public PaymentApiMock(String apiKey, double amount) {
        this.apiKey = apiKey;
        this.amount = amount;
    }

    public PaymentApiResponse charge(String apiKey, double amount) {
        if (apiKey == null || amount <= 0) {
            throw new IllegalArgumentException("apiKey and amount must be greater than 0");
        }
        else
            return new PaymentApiMock(apiKey, amount);
    }

    @Override
    public boolean isSuccess() {
        return false;
    }
}
