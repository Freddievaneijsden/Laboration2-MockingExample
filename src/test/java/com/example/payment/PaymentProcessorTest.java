package com.example.payment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class PaymentProcessorTest {

    PaymentProcessor paymentProcessor;
    DatabaseConnection databaseConnection;
    EmailServiceMock emailService;
    PaymentApi paymentApi;
    PaymentApiResponse paymentApiResponse;
    String testApiKey = "testApiKey";

    @BeforeEach
    void setUp() {
        databaseConnection = new DatabaseConnectionMock();
        emailService = new EmailServiceMock();
        paymentApi = new PaymentApiMock(); 
        paymentProcessor = new PaymentProcessor(testApiKey, databaseConnection, emailService, paymentApi);
    }

    @Test
    @DisplayName("Payment Processor Is Not Null")
    void paymentProcessorIsNotNull() {
        assertThat(paymentProcessor).isNotNull();
    }

    @Test
    @DisplayName("Charge Returns PaymentApiRespons When Given Valid ApiKey And Amount")
    void chargeReturnsPaymentApiResponsWhenGivenValidApiKeyAndAmount() {
        PaymentApiResponseMock expectedRespons = new PaymentApiResponseMock(testApiKey, true, 100);

        paymentApiResponse = paymentApi.charge(testApiKey, 100);

        assertThat(paymentApiResponse).isEqualTo(expectedRespons);
    }

    @Test
    @DisplayName("isSuccess should return true when charge has been called successfully")
    void isSuccessShouldReturnTrueWhenChargeHasBeenCalledSuccessfully() {
        PaymentApiResponseMock expectedRespons = new PaymentApiResponseMock(testApiKey, true, 100);

        paymentApiResponse = paymentApi.charge(testApiKey, 100);

        assertThat(paymentApiResponse.isSuccess()).isEqualTo(expectedRespons.isSuccess());
    }
}