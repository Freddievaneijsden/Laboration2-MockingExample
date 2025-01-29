package com.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BookingSystemTest {

    static Stream<Arguments> roomProvider() {
        return Stream.of(
                Arguments.of(new Room("1337", "Penthouse")),
                Arguments.of(new Room("2025", "Terrace")),
                Arguments.of(new Room("1969", "Moon")));
    }

    Room room = new Room("1337", "Penthouse");
    Room room2 = new Room("2025", "Terrace");
    Room room3 = new Room("1969", "Moon");
    RoomRepositoryMock roomRepositoryMock = new RoomRepositoryMock();
    TimeProviderMock timeProviderMock = new TimeProviderMock();
    LocalDateTime startTime = LocalDateTime.of(2025, 2, 15, 13, 30);
    LocalDateTime endTime = LocalDateTime.of(2025, 2, 16, 14, 10);
    LocalDateTime pastTime = LocalDateTime.of(2025, 1, 16, 12, 0);
    NotificationsServiceMock notificationsServiceMock = new NotificationsServiceMock();
    BookingSystem bookingSystem = new BookingSystem(timeProviderMock, roomRepositoryMock, notificationsServiceMock);

    @ParameterizedTest
    @MethodSource("roomProvider")
    @DisplayName("Available room should return true when booking")
    void availableRoomShouldReturnTrueWhenBooking(Room room) {
        roomRepositoryMock.save(room);
        boolean availableRoom = bookingSystem.bookRoom(room.getId(), startTime, endTime);
        assertThat(availableRoom).isEqualTo(true);
    }

    @ParameterizedTest
    @MethodSource("roomProvider")
    @DisplayName("Unavailable room should return false when booking")
    void UnavailableRoomShouldReturnFalseWhenBooking(Room room) {
        roomRepositoryMock.save(room);
        bookingSystem.bookRoom(room.getId(), startTime, endTime);

        boolean unavailableRoom = bookingSystem.bookRoom(room.getId(), startTime, endTime);
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
    @DisplayName("Booking room with invalid startTime throw exception")
    void bookingRoomWithInvalidStartTimeThrowException() {
        roomRepositoryMock.save(room);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            bookingSystem.bookRoom("1337", null, endTime);
        });
        assertThat(exception.getMessage()).isEqualTo("Bokning kräver giltiga start- och sluttider samt rum-id");
    }

    @Test
    @DisplayName("Booking room with invalid endTime throw exception")
    void bookingRoomWithInvalidEndTimeThrowException() {
        roomRepositoryMock.save(room);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            bookingSystem.bookRoom("1337", startTime, null);
        });
        assertThat(exception.getMessage()).isEqualTo("Bokning kräver giltiga start- och sluttider samt rum-id");
    }

    @Test
    @DisplayName("Booking room with invalid roomId throw exception")
    void bookingRoomWithInvalidRoomIdThrowException() {
        roomRepositoryMock.save(room);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            bookingSystem.bookRoom(null, startTime, endTime);
        });
        assertThat(exception.getMessage()).isEqualTo("Bokning kräver giltiga start- och sluttider samt rum-id");
    }

    @Test
    @DisplayName("Booking room were startTime is after endTime throw exception")
    void bookingRoomWereStartTimeIsAfterEndTimeThrowException() {
        roomRepositoryMock.save(room);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            bookingSystem.bookRoom("1337", endTime, startTime);
        });
        assertThat(exception.getMessage()).isEqualTo("Sluttid måste vara efter starttid");
    }

    @Test
    @DisplayName("Getting available rooms when startTime is invalid should throw exception")
    void gettingAvailableRoomsWhenStartTimeIsInvalidShouldThrowException() {
        roomRepositoryMock.save(room);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            bookingSystem.getAvailableRooms(null, endTime);
        });

        assertThat(exception.getMessage()).isEqualTo("Måste ange både start- och sluttid");
    }

    @Test
    @DisplayName("Getting available rooms when endTime is invalid should throw exception")
    void gettingAvailableRoomsWhenEndTimeIsInvalidShouldThrowException() {
        roomRepositoryMock.save(room);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            bookingSystem.getAvailableRooms(startTime, null);
        });

        assertThat(exception.getMessage()).isEqualTo("Måste ange både start- och sluttid");
    }

    @Test
    @DisplayName("Getting available rooms were startTime is after endTime throw exception")
    void gettingAvailableRoomsWereStartTimeIsAfterEndTimeThrowException() {
        roomRepositoryMock.save(room);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            bookingSystem.getAvailableRooms(endTime, startTime);
        });
        assertThat(exception.getMessage()).isEqualTo("Sluttid måste vara efter starttid");
    }

    @Test
    @DisplayName("Getting available rooms returns list of unbooked rooms")
    void gettingAvailableRoomsReturnsListOfUnbookedRooms() {
        roomRepositoryMock.save(room);
        roomRepositoryMock.save(room2);
        roomRepositoryMock.save(room3);

        bookingSystem.bookRoom("1337", startTime, endTime);

        List<Room> availableRooms = bookingSystem.getAvailableRooms(startTime, endTime);
        assertThat(availableRooms).isEqualTo(List.of(room2, room3));
    }

    @Test
    @DisplayName("Cancelling booking when argument is null should throw exception")
    void cancellingBookingWhenArgumentIsNullShouldThrowException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            bookingSystem.cancelBooking(null);
        });
        assertThat(exception.getMessage()).isEqualTo("Boknings-id kan inte vara null");
    }

    @Test
    @DisplayName("Cancelling booking should return true")
    void cancellingBookingShouldReturnTrue() {
        roomRepositoryMock.save(room);
        Booking booking = new Booking("Booking1234", room.getId(), startTime, endTime);
        room.addBooking(booking);
        boolean cancelledBooking = bookingSystem.cancelBooking("Booking1234");
        assertThat(cancelledBooking).isEqualTo(true);
    }

    @Test
    @DisplayName("Cancelling booking that does not exist return false")
    void cancellingBookingThatDoesNotExistReturnFalse() {
        roomRepositoryMock.save(room);
        Booking booking = new Booking("Booking1234", room.getId(), startTime, endTime);
        room.addBooking(booking);
        boolean cancelledBooking = bookingSystem.cancelBooking("Unknown booking");
        assertThat(cancelledBooking).isEqualTo(false);
    }

    @Test
    @DisplayName("Cancelling booking when startTime is before currentTime throws exception")
    void cancellingBookingWhenStartTimeIsBeforeCurrentTimeThrowsException() {
        roomRepositoryMock.save(room);
        Booking booking = new Booking("Booking1234", room.getId(), pastTime, endTime);
        room.addBooking(booking);
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            bookingSystem.cancelBooking("Booking1234");
        });
        assertThat(exception.getMessage()).isEqualTo("Kan inte avboka påbörjad eller avslutad bokning");
    }
}