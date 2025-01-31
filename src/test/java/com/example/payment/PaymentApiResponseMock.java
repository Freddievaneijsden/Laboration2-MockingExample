package com.example.payment;

import java.util.Objects;

public class PaymentApiResponseMock implements PaymentApiResponse {
    private final String paymentId;
    private final boolean paymentStatus;
    private final double amount;

    public PaymentApiResponseMock(String paymentId, boolean paymentStatus, double amount) {
        this.paymentId = paymentId;
        this.paymentStatus = paymentStatus;
        this.amount = amount;
    }

    @Override
    public boolean isSuccess() {
        if (paymentStatus) {
            return true;
        }
        else return false;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PaymentApiResponseMock that)) return false;
        return paymentStatus == that.paymentStatus && Double.compare(amount, that.amount) == 0 && Objects.equals(paymentId, that.paymentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paymentId, paymentStatus, amount);
    }
}
