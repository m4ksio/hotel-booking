package io.m4ks.recrutation.hotelbookings;

import io.m4ks.recrutation.hotelbookings.exceptions.RoomAlreadyBookedException;
import io.m4ks.recrutation.hotelbookings.exceptions.RoomDoesNotExistsException;

import java.time.LocalDate;

public interface BookingManager {
    /**
     * Return true if there is no booking for the given room on the date,
     * otherwise false
     */
    public boolean isRoomAvailable(Integer room, LocalDate date) throws RoomDoesNotExistsException;
    /**
     * Add a booking for the given guest in the given room on the given
     * date. If the room is not available, throw a suitable Exception.
     */
    public void addBooking(String guest, Integer room, LocalDate date) throws RoomAlreadyBookedException, RoomDoesNotExistsException;

    /**
     * Return a list of all the available room numbers for the given date
     */
    public Iterable<Integer> getAvailableRooms(LocalDate date);
}