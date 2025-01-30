package com.example;

import java.time.LocalDateTime;

public class TimeProviderMock implements TimeProvider {
  
    LocalDateTime FIXED_PRESENT_TIME = LocalDateTime.of(2025, 2, 14, 13, 30);
    @Override
    public LocalDateTime getCurrentTime() {
        return FIXED_PRESENT_TIME;
    }
}
