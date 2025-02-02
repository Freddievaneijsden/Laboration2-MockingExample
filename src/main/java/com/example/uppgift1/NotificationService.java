package com.example.uppgift1;

public interface NotificationService {
    void sendBookingConfirmation(Booking booking) throws NotificationException;
    void sendCancellationConfirmation(Booking booking) throws NotificationException;
}
