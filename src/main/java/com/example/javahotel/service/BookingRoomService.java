package com.example.javahotel.service;

import com.example.javahotel.entity.BookingRoom;

import java.util.List;

public interface BookingRoomService {
    void cancelBooking(Long bookingId);

    List<BookingRoom> getAllBookingsByRoomId(Long roomId);
}
