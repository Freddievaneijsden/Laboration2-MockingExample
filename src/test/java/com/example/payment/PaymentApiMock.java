package com.example.payment;

public class PaymentApiMock implements PaymentApi {

    public PaymentApiResponse charge(String apiKey, double amount) {
        if (apiKey == null || amount <= 0) {
            throw new IllegalArgumentException("apiKey cant be null and amount must be greater than 0");
        }
        else {
            return new PaymentApiResponseMock(apiKey, true, amount);
        }
    }
}
