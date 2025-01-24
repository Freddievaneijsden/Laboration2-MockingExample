package com.example;

public class NotificationsServiceMock implements NotificationService {
    @Override
    public void sendBookingConfirmation(Booking booking) throws NotificationException {
        System.out.println(booking + " has been confirmed");
    }

    @Override
    public void sendCancellationConfirmation(Booking booking) throws NotificationException {
        System.out.println(booking + " has been cancelled");
    }
}
