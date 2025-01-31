package com.example.payment;

import java.sql.SQLException;

public class PaymentProcessor {
    //Add constructor which take dependencies as argument for easier testing
    //Catch exception if update to database fails
    //Remove hardcoded API_KEY to avoid tests from being dependent on real API key
    //Remove getInstance() to avoid having to make mock PreparedStatement

    private final String API_KEY; //"sk_test_123456";
    private final DatabaseConnection databaseConnection;
    private final EmailService emailService;
    private final PaymentApi paymentApi;

    public PaymentProcessor(String apiKey, DatabaseConnection databaseConnection, EmailService emailService, PaymentApi paymentApi) {
        this.API_KEY = apiKey;
        this.databaseConnection = databaseConnection;
        this.emailService = emailService;
        this.paymentApi = paymentApi;
    }

    public boolean processPayment(double amount) {
        // Anropar extern betaltjänst direkt med statisk API-nyckel
        PaymentApiResponse paymentApiResponse = paymentApi.charge(API_KEY, amount);

        // Skriver till databas direkt
        if (paymentApiResponse.isSuccess()) {
            try {
                databaseConnection.executeUpdate("INSERT INTO payments (amount, status) VALUES (" + amount + ", 'SUCCESS')");
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
