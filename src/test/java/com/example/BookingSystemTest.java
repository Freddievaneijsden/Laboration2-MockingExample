package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingSystemTest {

    /*
    What operation are we testing?
    Under what circumstances?
    What is the expected result?
    UnitOfWork_StateUnderTest_ExpectedBehavior
     */

    static Stream<Arguments> roomProvider() {
        return Stream.of(
                Arguments.of(new Room("1337", "Penthouse")),
                Arguments.of(new Room("2025", "Terrace")),
                Arguments.of(new Room("1969", "Moon")));
    }

    Room room;
    Room room2;
    Room room3;
    RoomRepositoryMock roomRepositoryMock;
    TimeProviderMock timeProviderMock;
    LocalDateTime startTime;
    LocalDateTime endTime;
    LocalDateTime pastTime;
    NotificationsServiceMock notificationsServiceMock;
    BookingSystem bookingSystem;
    LocalDateTime currentTimeMockito;
    @Mock
    RoomRepository roomRepositoryMockito;
    @Mock
    TimeProvider timeProviderMockito;
    @Mock
    NotificationService notificationServiceMockito;
    @Mock
    Room roomMockito;
    @InjectMocks
    BookingSystem bookingSystemMockito;

    @BeforeEach
    void setUp() {
        currentTimeMockito = LocalDateTime.of(2025, 2, 14, 13, 30);
        room = new Room("1337", "Penthouse");
        room2 = new Room("2025", "Terrace");
        room3 = new Room("1969", "Moon");
        roomRepositoryMock = new RoomRepositoryMock();
        timeProviderMock = new TimeProviderMock();
        startTime = LocalDateTime.of(2025, 2, 15, 13, 30);
        endTime = LocalDateTime.of(2025, 2, 16, 14, 10);
        pastTime = LocalDateTime.of(2025, 1, 16, 12, 0);
        notificationsServiceMock = new NotificationsServiceMock();
        bookingSystem = new BookingSystem(timeProviderMock, roomRepositoryMock, notificationsServiceMock);
        roomRepositoryMock.save(room);
        roomRepositoryMock.save(room2);
        roomRepositoryMock.save(room3);
    }

    @ParameterizedTest
    @MethodSource("roomProvider")
    @DisplayName("Booking room that is available should return true")
    void bookingRoomThatIsAvailableShouldReturnTrue(Room room) {
        boolean availableRoom = bookingSystem.bookRoom(room.getId(), startTime, endTime);

        assertThat(availableRoom).isEqualTo(true);
    }

    @ParameterizedTest
    @MethodSource("roomProvider")
    @DisplayName("Booking room that is unavailable should return false")
    void bookingRoomThatIsUnavailableShouldReturnFalse(Room room) {
        bookingSystem.bookRoom(room.getId(), startTime, endTime);

        boolean unavailableRoom = bookingSystem.bookRoom(room.getId(), startTime, endTime);

        assertThat(unavailableRoom).isEqualTo(false);
    }

    @Test
    @DisplayName("Booking room that is available should return true using mockito")
    void bookingRoomThatIsAvailableShouldReturnTrueUsingMockito() {
        when(roomMockito.getId()).thenReturn("1337");
        when(roomMockito.isAvailable(startTime, endTime)).thenReturn(true);

        when(roomRepositoryMockito.findById("1337")).thenReturn(Optional.of(roomMockito));
        doNothing().when(roomRepositoryMockito).save(roomMockito);

        when(timeProviderMockito.getCurrentTime()).thenReturn(currentTimeMockito);

        boolean availableRoom = bookingSystemMockito.bookRoom(roomMockito.getId(), startTime, endTime);

        assertThat(availableRoom).isEqualTo(true);
    }

    @Test
    @DisplayName("Booking room in past time throws exception")
    void bookingRoomInPastTimeThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            bookingSystem.bookRoom("1337", pastTime, endTime);
        });

        assertThat(exception.getMessage()).isEqualTo("Kan inte boka tid i dåtid");
    }

    @Test
    @DisplayName("Booking room that does not exist throws exception")
    void bookingRoomThatDoesNotExistThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            bookingSystem.bookRoom("1336", startTime, endTime);
        });

        assertThat(exception.getMessage()).isEqualTo("Rummet existerar inte");
    }

    @Test
    @DisplayName("Booking room with invalid startTime throw exception")
    void bookingRoomWithInvalidStartTimeThrowException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            bookingSystem.bookRoom("1337", null, endTime);
        });

        assertThat(exception.getMessage()).isEqualTo("Bokning kräver giltiga start- och sluttider samt rum-id");
    }

    @Test
    @DisplayName("Booking room with invalid endTime throw exception")
    void bookingRoomWithInvalidEndTimeThrowException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            bookingSystem.bookRoom("1337", startTime, null);
        });

        assertThat(exception.getMessage()).isEqualTo("Bokning kräver giltiga start- och sluttider samt rum-id");
    }

    @Test
    @DisplayName("Booking room with invalid roomId throw exception")
    void bookingRoomWithInvalidRoomIdThrowException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            bookingSystem.bookRoom(null, startTime, endTime);
        });

        assertThat(exception.getMessage()).isEqualTo("Bokning kräver giltiga start- och sluttider samt rum-id");
    }

    @Test
    @DisplayName("Booking room when startTime is after endTime throw exception")
    void bookingRoomWhenStartTimeIsAfterEndTimeThrowException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            bookingSystem.bookRoom("1337", endTime, startTime);
        });

        assertThat(exception.getMessage()).isEqualTo("Sluttid måste vara efter starttid");
    }

    @Test
    @DisplayName("Getting available rooms when startTime is invalid should throw exception")
    void gettingAvailableRoomsWhenStartTimeIsInvalidShouldThrowException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            bookingSystem.getAvailableRooms(null, endTime);
        });

        assertThat(exception.getMessage()).isEqualTo("Måste ange både start- och sluttid");
    }

    @Test
    @DisplayName("Getting available rooms when endTime is invalid should throw exception")
    void gettingAvailableRoomsWhenEndTimeIsInvalidShouldThrowException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            bookingSystem.getAvailableRooms(startTime, null);
        });

        assertThat(exception.getMessage()).isEqualTo("Måste ange både start- och sluttid");
    }

    @Test
    @DisplayName("Getting available rooms when startTime is after endTime throw exception")
    void gettingAvailableRoomsWhenStartTimeIsAfterEndTimeThrowException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            bookingSystem.getAvailableRooms(endTime, startTime);
        });

        assertThat(exception.getMessage()).isEqualTo("Sluttid måste vara efter starttid");
    }

    @Test
    @DisplayName("Getting available rooms returns list of unbooked rooms")
    void gettingAvailableRoomsReturnsListOfUnbookedRooms() {
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

    @ParameterizedTest
    @MethodSource("roomProvider")
    @DisplayName("Cancelling booking should return true")
    void cancellingBookingShouldReturnTrue(Room room) {
        roomRepositoryMock.save(room);
        Booking booking = new Booking("Booking1234", room.getId(), startTime, endTime);
        room.addBooking(booking);
        boolean cancelledBooking = bookingSystem.cancelBooking("Booking1234");

        assertThat(cancelledBooking).isEqualTo(true);
    }

    @Test
    @DisplayName("Cancelling booking that does not exist return false")
    void cancellingBookingThatDoesNotExistReturnFalse() {
        Booking booking = new Booking("Booking1234", room.getId(), startTime, endTime);
        room.addBooking(booking);
        boolean cancelledBooking = bookingSystem.cancelBooking("Unknown booking");

        assertThat(cancelledBooking).isEqualTo(false);
    }

    @Test
    @DisplayName("Cancelling booking when startTime is before currentTime throws exception")
    void cancellingBookingWhenStartTimeIsBeforeCurrentTimeThrowsException() {
        Booking booking = new Booking("Booking1234", room.getId(), pastTime, endTime);
        room.addBooking(booking);
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            bookingSystem.cancelBooking("Booking1234");
        });

        assertThat(exception.getMessage()).isEqualTo("Kan inte avboka påbörjad eller avslutad bokning");
    }
}