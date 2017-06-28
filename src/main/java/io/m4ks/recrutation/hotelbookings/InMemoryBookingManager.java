package io.m4ks.recrutation.hotelbookings;

import io.m4ks.recrutation.hotelbookings.exceptions.RoomAlreadyBookedException;
import io.m4ks.recrutation.hotelbookings.exceptions.RoomDoesNotExistsException;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryBookingManager implements BookingManager {

    private ConcurrentHashMap<LocalDate, Set<Integer>> reservations = new ConcurrentHashMap<>();

    private static class RoomError extends Error {};

    private final Set<Integer> rooms;

    public InMemoryBookingManager(Set<Integer> rooms) {
        this.rooms = new HashSet<>(rooms);
    }

    private void ensureRoom(Integer room) throws RoomDoesNotExistsException {
        if (!rooms.contains(room)) {
            throw new RoomDoesNotExistsException();
        }
    }

    @Override
    public boolean isRoomAvailable(Integer room, LocalDate date) throws RoomDoesNotExistsException {
        ensureRoom(room);
        return !reservations.getOrDefault(date, Collections.emptySet()).contains(room);
    }

    @Override
    public void addBooking(String guest, Integer room, LocalDate date) throws RoomDoesNotExistsException, RoomAlreadyBookedException {

        Set<Integer> rooms = new HashSet<>();
        rooms.add(room);

        try {
            reservations.merge(date, rooms, (d, oldRooms) -> {
                if (oldRooms.contains(room)) {
                    throw new RoomError();
                } else {
                    HashSet<Integer> newRooms = new HashSet<>(oldRooms);
                    newRooms.add(room);
                    return newRooms;
                }
            });
        } catch (RoomError re) {
            throw new RoomAlreadyBookedException();
        }
    }

    @Override
    public Iterable<Integer> getAvailableRooms(LocalDate date) {

        Set<Integer> bookedRooms = new HashSet<>(reservations.getOrDefault(date, Collections.emptySet()));

        HashSet<Integer> roomsCopy = new HashSet<>(rooms);
        roomsCopy.removeAll(bookedRooms);
        return roomsCopy;
    }
}
