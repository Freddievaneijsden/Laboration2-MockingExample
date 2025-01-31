package com.example.payment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class PaymentProcessorTest {

    PaymentProcessor paymentProcessor;
    DatabaseConnectionMock databaseConnection;
    EmailServiceMock emailService;
    PaymentApi paymentApi;
    PaymentApiResponse paymentApiResponse;
    String testApiKey = "testApiKey";
    String testEmail = "testEmail";

    @BeforeEach
    void setUp() {
        databaseConnection = new DatabaseConnectionMock();
        emailService = new EmailServiceMock();
        paymentApi = new PaymentApiMock(); 
        paymentProcessor = new PaymentProcessor(testApiKey, databaseConnection, emailService, paymentApi);
    }

    @Test
    @DisplayName("PaymentProcessor Is Not Null")
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
    @DisplayName("IsSuccess should return true when charge has been called successfully")
    void isSuccessShouldReturnTrueWhenChargeHasBeenCalledSuccessfully() {
        PaymentApiResponseMock expectedRespons = new PaymentApiResponseMock(testApiKey, true, 100);

        paymentApiResponse = paymentApi.charge(testApiKey, 100);

        assertThat(paymentApiResponse.isSuccess()).isEqualTo(expectedRespons.isSuccess());
    }

    @Test
    @DisplayName("SendPaymentConfirmation send email when email and amount is valid")
    void sendPaymentConfirmationSendEmailWhenEmailAndAmountIsValid() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        emailService.sendPaymentConfirmation(testEmail, 100);

        System.setOut(originalOut);

        assertThat(outputStream.toString().trim()).isEqualTo(("Email sent to testEmail confirming payment of 100.0"));
    }

    @Test
    @DisplayName("ExecuteUpdate should update wasUpdateCalled to true")
    void executeUpdateShouldUpdateWasUpdateCalledToTrue() {
        boolean beforeCalledUpdate = databaseConnection.wasUpdateCalled();

        databaseConnection.executeUpdate("INSERT INTO payments (amount, status) VALUES (" + 100 + ", 'SUCCESS')");
        boolean afterCalledUpdate = databaseConnection.wasUpdateCalled();

        assertThat(databaseConnection.getLastQuery()).isEqualTo("INSERT INTO payments (amount, status) VALUES (" + 100 + ", 'SUCCESS')");
        assertAll(
                "Validating database update",
                () -> assertThat(beforeCalledUpdate).isFalse(),
                () -> assertThat(afterCalledUpdate).isTrue(),
                () -> assertThat(databaseConnection.getLastQuery()).isEqualTo("INSERT INTO payments (amount, status) VALUES (" + 100 + ", 'SUCCESS')")
        );
    }

}