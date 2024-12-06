package com.example.javahotel.service.impl;

import com.example.javahotel.entity.BookingRoom;
import com.example.javahotel.entity.Room;
import com.example.javahotel.exception.InvalidBookingRequestException;
import com.example.javahotel.exception.ResourceNotFoundException;
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

    @Override
    public List<BookingRoom> getAllBookings() {
        return bookingRoomRepository.findAll();
    }

    @Override
    public String savedBooking(Long roomId, BookingRoom bookingRequest) {
        if (bookingRequest.getCheckOutDate().isBefore(bookingRequest.getCheckInDate())) {
            throw new InvalidBookingRequestException("Check out date must be after check in date");
        }
        Room room = roomService.getRoomById(roomId).get();
        List<BookingRoom> existingBookings = room.getBookings();
        boolean roomIsAvailable = roomIsAvailable(bookingRequest, existingBookings);
        if (roomIsAvailable) {
            room.addBooking(bookingRequest);
            bookingRoomRepository.save(bookingRequest);
        } else {
            throw new InvalidBookingRequestException("Room is not available for the requested dates");
        }
        return bookingRequest.getBookingConfirmationCode();
    }

    @Override
    public BookingRoom findByBookingConfirmationCode(String confirmationCode) {
        return bookingRoomRepository.findByBookingConfirmationCode(confirmationCode).orElseThrow(()-> new ResourceNotFoundException("No booking found with confirmation code: " + confirmationCode));
    }

    @Override
    public List<BookingRoom> findBookingsByUserEmail(String email) {
        return bookingRoomRepository.findByGuestEmail(email);
    }

    private boolean roomIsAvailable(BookingRoom bookingRequest, List<BookingRoom> existingBookings) {
        return existingBookings.stream()
                .noneMatch(existingBooking ->
                        bookingRequest.getCheckInDate().equals(existingBooking.getCheckInDate())
                                || bookingRequest.getCheckOutDate().isBefore(existingBooking.getCheckOutDate())
                                || (bookingRequest.getCheckInDate().isAfter(existingBooking.getCheckInDate())
                                && bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())
                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().isBefore(existingBooking.getCheckInDate())
                                && bookingRequest.getCheckOutDate().isAfter(existingBooking.getCheckOutDate()))
                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(existingBooking.getCheckInDate()))
                                || (bookingRequest.getCheckInDate().equals(existingBooking.getCheckOutDate())
                                && bookingRequest.getCheckOutDate().equals(bookingRequest.getCheckInDate()))
                );
    }
}
