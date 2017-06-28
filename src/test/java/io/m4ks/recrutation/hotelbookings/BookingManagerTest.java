package io.m4ks.recrutation.hotelbookings;

import io.m4ks.recrutation.hotelbookings.exceptions.RoomAlreadyBookedException;
import io.m4ks.recrutation.hotelbookings.exceptions.RoomDoesNotExistsException;
import org.junit.Test;

import java.time.LocalDate;
import java.util.*;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.*;

public class BookingManagerTest {

    private static final HashSet<Integer> rooms;

    private static LocalDate today = LocalDate.parse("2012-07-21");
    private static LocalDate tomorrow = LocalDate.parse("2012-07-22");

    static {
        rooms = new HashSet<>();
        rooms.add(101);
        rooms.add(102);
        rooms.add(201);
        rooms.add(203);
    }

    @Test
    public void roomAvailability() throws Exception {

        BookingManager bm = new InMemoryBookingManager(rooms);

        assertTrue(bm.isRoomAvailable(101, today));
    }

    @Test
    public void booking() throws RoomAlreadyBookedException, RoomDoesNotExistsException {

        BookingManager bm = new InMemoryBookingManager(rooms);

        bm.addBooking("Smith", 101, today);
        assertFalse(bm.isRoomAvailable(101, today));
    }

    @Test
    public void bookingOnDifferentDates() throws RoomAlreadyBookedException, RoomDoesNotExistsException {

        BookingManager bm = new InMemoryBookingManager(rooms);

        bm.addBooking("Smith", 101, today);
        assertFalse(bm.isRoomAvailable(101, today));
        assertTrue(bm.isRoomAvailable(101, tomorrow));
    }

    @Test(expected = RoomAlreadyBookedException.class)
    public void doubleBooking() throws RoomAlreadyBookedException, RoomDoesNotExistsException {

        BookingManager bm = new InMemoryBookingManager(rooms);

        bm.addBooking("Jones", 101, today);
        bm.addBooking("Jones", 101, today);
    }

    @Test(expected = RoomDoesNotExistsException.class)
    public void checkingWrongRoom() throws Exception {

        BookingManager bm = new InMemoryBookingManager(rooms);

        bm.isRoomAvailable(666, today);
    }

    @Test(expected = RoomDoesNotExistsException.class)
    public void testBookingWrongRoom() throws Exception {

        BookingManager bm = new InMemoryBookingManager(rooms);

        bm.isRoomAvailable(666, today);
    }

    @Test
    public void allRoomsFreeWithNoBookings() throws Exception {
        BookingManager bm = new InMemoryBookingManager(rooms);

        Iterable<Integer> availableRooms = bm.getAvailableRooms(today);

        assertEquals(availableRooms, rooms);
    }

    @Test
    public void nonbookedRoomsFree() throws Exception {
        BookingManager bm = new InMemoryBookingManager(rooms);

        bm.addBooking("Whatever", 102, today);
        Iterable<Integer> availableRooms = bm.getAvailableRooms(today);

        assertThat(availableRooms, hasItems(101, 201, 203));
        assertThat(availableRooms, not(hasItems(102)));
    }


}