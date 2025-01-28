package com.example;

import java.time.LocalDateTime;

public class TimeProviderMock implements TimeProvider {

    @Override
    public LocalDateTime getCurrentTime() {
        return LocalDateTime.now();
    }
}
