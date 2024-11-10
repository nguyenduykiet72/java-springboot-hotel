package com.example.javahotel.service.impl;

import com.example.javahotel.entity.BookingRoom;
import com.example.javahotel.repository.BookingRoomRepository;
import com.example.javahotel.service.BookingRoomService;
import com.example.javahotel.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingRoomServiceImpl implements BookingRoomService {

    private final BookingRoomRepository bookingRoomRepository;
    private final RoomService roomService;

    @Override
    public void cancelBooking(Long bookingId) {
        bookingRoomRepository.deleteById(bookingId);
    }

    public List<BookingRoom> getAllBookingsByRoomId(Long roomId) {
        return bookingRoomRepository.findByRoomId(roomId);
    }
}
