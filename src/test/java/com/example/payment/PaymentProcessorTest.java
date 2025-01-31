package com.example.payment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentProcessorTest {

    PaymentProcessor paymentProcessor;
    DatabaseConnectionMock databaseConnection;
    EmailServiceMock emailService;
    PaymentApi paymentApi;
    PaymentApiResponse paymentApiResponse;
    String testApiKey = "testApiKey";
    String testEmail = "testEmail";
    @Mock
    PaymentApi paymentApiMockito;

    @BeforeEach
    void setUp() {
        databaseConnection = new DatabaseConnectionMock();
        emailService = new EmailServiceMock();
        paymentApi = new PaymentApiMock(); 
        paymentProcessor = new PaymentProcessor(testApiKey, databaseConnection, emailService, paymentApi);
    }

    @Test
    @DisplayName("PaymentProcessor is not null")
    void paymentProcessorIsNotNull() {
        assertThat(paymentProcessor).isNotNull();
    }

    @Test
    @DisplayName("Charge returns paymentApiResponse when given valid apiKey and amount")
    void chargeReturnsPaymentApiResponseWhenGivenValidApiKeyAndAmount() {
        PaymentApiResponseMock expectedResponse = new PaymentApiResponseMock(testApiKey, true, 100);

        paymentApiResponse = paymentApi.charge(testApiKey, 100);

        assertThat(paymentApiResponse).isEqualTo(expectedResponse);
    }

    @Test
    @DisplayName("IsSuccess return true when charge is successful")
    void isSuccessReturnTrueWhenChargeIsSuccessful() {
        PaymentApiResponseMock expectedResponse = new PaymentApiResponseMock(testApiKey, true, 100);

        paymentApiResponse = paymentApi.charge(testApiKey, 100);

        assertThat(paymentApiResponse.isSuccess()).isEqualTo(expectedResponse.isSuccess());
    }

    @Test
    @DisplayName("SendPaymentConfirmation should send email for valid email and amount")
    void sendPaymentConfirmationShouldSendEmailForValidEmailAndAmount() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        emailService.sendPaymentConfirmation(testEmail, 100);

        System.setOut(originalOut);

        assertThat(outputStream.toString().trim()).isEqualTo(("Email sent to testEmail confirming payment of 100.0"));
    }

    @Test
    @DisplayName("ExecuteUpdate should mark update as called")
    void executeUpdateShouldMarkUpdateAsCalled() {
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

    @Test
    @DisplayName("ProcessPayment return true when payment is successful")
    void processPaymentReturnTrueWhenPaymentIsSuccessful() {
        boolean successfulPayment = paymentProcessor.processPayment(100);

        assertThat(successfulPayment).isEqualTo(true);
    }

    @Test
    @DisplayName("ProcessPayment fails when PaymentApiResponse is unsuccessful")
    void processPaymentFailsWhenPaymentApiResponseIsUnsuccessful() {
        PaymentProcessor paymentProcessorWithMockito = new PaymentProcessor(testApiKey, databaseConnection, emailService, paymentApiMockito);
        when(paymentApiMockito.charge(testApiKey, 100)).thenReturn(new PaymentApiResponseMock(testApiKey, false, 100));

        boolean failedPayment = paymentProcessorWithMockito.processPayment(100.0);

        assertThat(failedPayment).isEqualTo(false);
    }

}