package com.example.bookingSystem;

import com.example.BookingSystem;
import com.example.Room;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class BookingSystemTest {
    Room room = new Room("1337", "Penthouse");
    RoomRepositoryMock roomRepositoryMock = new RoomRepositoryMock();
    TimeProviderMock timeProviderMock = new TimeProviderMock();
    LocalDateTime startTime = LocalDateTime.of(2025, 2, 15, 13, 30);
    LocalDateTime endTime = LocalDateTime.of(2025, 2, 16, 14, 10);
    NotificationsServiceMock notificationsServiceMock = new NotificationsServiceMock();

    @Test
    @DisplayName("BookARoomIfAvailable")
    void bookARoomIfAvailable() {
        roomRepositoryMock.save(room);
        BookingSystem bookingSystem = new BookingSystem(timeProviderMock, roomRepositoryMock, notificationsServiceMock);
        boolean availableRoom =  bookingSystem.bookRoom("1337", startTime, endTime);
        assertThat(availableRoom).isTrue();
    }
}