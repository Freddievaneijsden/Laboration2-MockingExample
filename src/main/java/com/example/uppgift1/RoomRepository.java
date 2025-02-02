package com.example.uppgift1;

import java.util.List;
import java.util.Optional;

public interface RoomRepository {
    Optional<Room> findById(String id);
    List<Room> findAll();
    void save(Room room);
}
