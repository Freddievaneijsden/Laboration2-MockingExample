package com.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class BookingSystemTest {
    Room room = new Room("1337", "Penthouse");
    Room room2 = new Room("2025", "Terrace");
    Room room3 = new Room("1969", "Moon");
    RoomRepositoryMock roomRepositoryMock = new RoomRepositoryMock();
    TimeProviderMock timeProviderMock = new TimeProviderMock();
    LocalDateTime startTime = LocalDateTime.of(2025, 2, 15, 13, 30);
    LocalDateTime endTime = LocalDateTime.of(2025, 2, 16, 14, 10);
    LocalDateTime pastTime = LocalDateTime.of(2025, 1, 16, 12, 0);
    LocalDateTime futureTime = LocalDateTime.of(2025, 3, 16, 15, 0);
    NotificationsServiceMock notificationsServiceMock = new NotificationsServiceMock();
    BookingSystem bookingSystem = new BookingSystem(timeProviderMock, roomRepositoryMock, notificationsServiceMock);

    @Test
    @DisplayName("Available room should return true when booking")
    void availableRoomShouldReturnTrueWhenBooking() {
        roomRepositoryMock.save(room);
        boolean availableRoom = bookingSystem.bookRoom("1337", startTime, endTime);
        assertThat(availableRoom).isEqualTo(true);
    }

    @Test
    @DisplayName("Unavailable room should return false when booking")
    void UnavailableRoomShouldReturnFalseWhenBooking() {
        roomRepositoryMock.save(room);
        bookingSystem.bookRoom("1337", startTime, endTime);

        boolean unavailableRoom =  bookingSystem.bookRoom("1337", startTime, endTime);
        assertThat(unavailableRoom).isEqualTo(false);
    }

    @Test
    @DisplayName("Booking room in past time throws exception")
    void bookingRoomInPastTimeThrowsException() {
        roomRepositoryMock.save(room);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            bookingSystem.bookRoom("1337", pastTime, endTime);
        });

        assertThat(exception.getMessage()).isEqualTo("Kan inte boka tid i dåtid");
    }

    @Test
    @DisplayName("Booking room that does not exist throws exception")
    void bookingRoomThatDoesNotExistThrowsException() {
        roomRepositoryMock.save(room);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            bookingSystem.bookRoom("1336", startTime, endTime);
        });

        assertThat(exception.getMessage()).isEqualTo("Rummet existerar inte");
    }

    @Test
    @DisplayName("Booking room requires valid startTime")
    void bookingRoomRequiresValidStartTime() {
        roomRepositoryMock.save(room);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            bookingSystem.bookRoom("1337", null, endTime);
        });
        assertThat(exception.getMessage()).isEqualTo("Bokning kräver giltiga start- och sluttider samt rum-id");
    }

    @Test
    @DisplayName("Booking room requires valid endTime")
    void bookingRoomRequiresValidEndTime() {
        roomRepositoryMock.save(room);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            bookingSystem.bookRoom("1337", startTime, null);
        });
        assertThat(exception.getMessage()).isEqualTo("Bokning kräver giltiga start- och sluttider samt rum-id");
    }

    @Test
    @DisplayName("Booking room requires valid room")
    void bookingRoomRequiresValidRoom() {
        roomRepositoryMock.save(room);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            bookingSystem.bookRoom(null, startTime, endTime);
        });
        assertThat(exception.getMessage()).isEqualTo("Bokning kräver giltiga start- och sluttider samt rum-id");
    }

    @Test
    @DisplayName("Booking room requires startTime to be before endTime ")
    void bookingRoomRequiresStartTimeToBeBeforeEndTime() {
        roomRepositoryMock.save(room);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            bookingSystem.bookRoom("1337", endTime, startTime);
        });
        assertThat(exception.getMessage()).isEqualTo("Sluttid måste vara efter starttid");
    }

    @Test
    @DisplayName("Showing available rooms requires valid startTime")
    void showingAvailableRoomsRequiresValidStartTime() {
        roomRepositoryMock.save(room);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            bookingSystem.getAvailableRooms(null, endTime);
        });

        assertThat(exception.getMessage()).isEqualTo("Måste ange både start- och sluttid");
    }

    @Test
    @DisplayName("Showing available rooms requires valid endTime")
    void showingAvailableRoomsRequiresValidEndTime() {
        roomRepositoryMock.save(room);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            bookingSystem.getAvailableRooms(startTime, null);
        });

        assertThat(exception.getMessage()).isEqualTo("Måste ange både start- och sluttid");
    }

    @Test
    @DisplayName("Showing available rooms requires startTime to be before endTime ")
    void showingAvailableRoomsRequiresStartTimeToBeBeforeEndTime() {
        roomRepositoryMock.save(room);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            bookingSystem.getAvailableRooms(endTime, startTime);
        });
        assertThat(exception.getMessage()).isEqualTo("Sluttid måste vara efter starttid");
    }

    @Test
    @DisplayName("Showing available rooms should get unbooked rooms")
    void showingAvailableRoomsShouldGetUnbookedRooms() {
        roomRepositoryMock.save(room);
        roomRepositoryMock.save(room2);
        roomRepositoryMock.save(room3);

        bookingSystem.bookRoom("1337", startTime, endTime);

        List<Room> availableRooms = bookingSystem.getAvailableRooms(startTime, endTime);
        assertThat(availableRooms).isEqualTo(List.of(room2, room3));
    }
}