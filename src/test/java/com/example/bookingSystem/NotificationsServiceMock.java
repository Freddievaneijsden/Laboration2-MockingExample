package com.example.bookingSystem;

import com.example.Booking;
import com.example.NotificationException;
import com.example.NotificationService;

import java.sql.SQLOutput;

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
