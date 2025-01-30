package com.example.payment;

import java.sql.SQLException;

public class PaymentProcessor {
    //Add constructor which take dependencies as argument for easier testing
    //Catch exception if update to database fails


    private static final String API_KEY = "sk_test_123456";
    private final DatabaseConnection databaseConnection;
    private final EmailService emailService;
    private final PaymentApi paymentApi;
    private PaymentApiResponse paymentApiResponse;

    public PaymentProcessor(DatabaseConnection databaseConnection, EmailService emailService, PaymentApi paymentApi, PaymentApi paymentApi1) {
        this.databaseConnection = databaseConnection;
        this.emailService = emailService;
        this.paymentApi = paymentApi1;
    }

    public boolean processPayment(double amount) {
        // Anropar extern betaltjänst direkt med statisk API-nyckel
        paymentApiResponse = paymentApi.charge(API_KEY, amount);

        // Skriver till databas direkt
        if (paymentApiResponse.isSuccess()) {
            try {
                databaseConnection.getInstance().executeUpdate("INSERT INTO payments (amount, status) VALUES (" + amount + ", 'SUCCESS')");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // Skickar e-post direkt
        if (paymentApiResponse.isSuccess()) {
            emailService.sendPaymentConfirmation("user@example.com", amount);
        }

        return paymentApiResponse.isSuccess();
    }
}
