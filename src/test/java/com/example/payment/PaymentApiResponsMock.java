package com.example.payment;

public class PaymentApiResponsMock implements PaymentApiResponse {
    @Override
    public boolean isSuccess() {
        return false;
    }
}
