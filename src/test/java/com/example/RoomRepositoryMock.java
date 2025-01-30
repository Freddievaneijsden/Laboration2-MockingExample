package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RoomRepositoryMock
implements RoomRepository {

    public List<Room> rooms = new ArrayList<>();

    @Override
    public Optional<Room> findById(String id) {
         return rooms.stream()
                 .filter(room -> room.getId().equals(id))
                 .findFirst();
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
