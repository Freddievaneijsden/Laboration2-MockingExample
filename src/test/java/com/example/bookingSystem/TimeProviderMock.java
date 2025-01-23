package com.example.bookingSystem;

import com.example.TimeProvider;

import java.time.LocalDateTime;

public class TimeProviderMock implements TimeProvider {

    @Override
    public LocalDateTime getCurrentTime() {
        return LocalDateTime.now();
    }
}
