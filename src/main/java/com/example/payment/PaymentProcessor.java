package com.example.payment;

import java.sql.SQLException;

public class PaymentProcessor {
    //Add constructor which take dependencies as argument for easier testing
    //Failed update to database will catch exception

    private static final String API_KEY = "sk_test_123456";
    private final DatabaseConnection databaseConnection;
    private final EmailService emailService;

    public PaymentProcessor(DatabaseConnection databaseConnection, EmailService emailService) {
        this.databaseConnection = databaseConnection;
        this.emailService = emailService;
    }

    public boolean processPayment(double amount) {
        // Anropar extern betaltjänst direkt med statisk API-nyckel
        PaymentApiResponse response = PaymentApi.charge(API_KEY, amount);

        // Skriver till databas direkt
        if (response.isSuccess()) {
            try {
                databaseConnection.getInstance().executeUpdate("INSERT INTO payments (amount, status) VALUES (" + amount + ", 'SUCCESS')");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // Skickar e-post direkt
        if (response.isSuccess()) {
            emailService.sendPaymentConfirmation("user@example.com", amount);
        }

        return response.isSuccess();
    }
}
