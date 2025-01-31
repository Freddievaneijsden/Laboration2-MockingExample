package com.example.payment;

public class EmailServiceMock implements EmailService {
    @Override
    public void sendPaymentConfirmation(String mail, double amount) {
        if (mail == null) {
            throw new IllegalArgumentException("Mail must not be null");
        }
        else if (amount < 0) {
            throw new IllegalArgumentException("Amount must not be negative");
        }
        else {
            System.out.println("Email sent to " + mail + " confirming payment of " + amount);
        }
    }
}
