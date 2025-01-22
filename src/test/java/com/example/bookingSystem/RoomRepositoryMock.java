package com.example.bookingSystem;

import com.example.Room;
import com.example.RoomRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoomRepositoryMock
implements RoomRepository {

    public List<Room> rooms = new ArrayList<>();

    @Override
    public Optional<Room> findById(String id) {
        Room searchedRoom = null;
        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i).getId().equals(id)) {
                searchedRoom = rooms.get(i);
            } else return Optional.empty();
        }
        if (searchedRoom == null)
            return Optional.empty();

        return Optional.of(searchedRoom);
    }

    @Override
    public List<Room> findAll() {
        return rooms;
    }

    @Override
    public void save(Room room) {
        rooms.add(room);
    }
}
