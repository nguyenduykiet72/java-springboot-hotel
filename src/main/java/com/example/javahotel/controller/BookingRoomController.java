package com.example.javahotel.controller;

import com.example.javahotel.dto.response.BookingDTO;
import com.example.javahotel.dto.response.RoomDTO;
import com.example.javahotel.entity.BookingRoom;
import com.example.javahotel.entity.Room;
import com.example.javahotel.exception.InvalidBookingRequestException;
import com.example.javahotel.exception.ResourceNotFoundException;
import com.example.javahotel.service.BookingRoomService;
import com.example.javahotel.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/bookings")
public class BookingRoomController {

    private final BookingRoomService bookingRoomService;
    private final RoomService roomService;

    private BookingDTO getBookingResponse(BookingRoom booking) {
        Room theRoom = roomService.getRoomById(booking.getRoom().getId()).get();
        RoomDTO room = new RoomDTO(
                theRoom.getId(),
                theRoom.getRoomType(),
                theRoom.getRoomPrice()
        );
        return new BookingDTO(
                booking.getBookingId(), booking.getCheckInDate(),
                booking.getCheckOutDate(), booking.getGuestFullName(),
                booking.getGuestEmail(), booking.getNumberOfAdults(),
                booking.getNumberOfChildren(), booking.getTotalNumberOfGuest(),
                booking.getBookingConfirmationCode(), room);
    }

    @GetMapping("/all-bookings")
    public ResponseEntity<List<BookingDTO>> getAllBookings() {
        List<BookingRoom> bookings = bookingRoomService.getAllBookings();
        List<BookingDTO> bookingDTOS = new ArrayList<>();

        for (BookingRoom booking : bookings) {
            BookingDTO bookingDTO = getBookingResponse(booking);
            bookingDTOS.add(bookingDTO);
        }
        return ResponseEntity.ok(bookingDTOS);
    }

    @PostMapping("/room/{roomId}/booking")
    public ResponseEntity<?> saveBooking(@PathVariable Long roomId,
                                         @RequestBody BookingRoom bookingRequest){
        try {
            String confirmationCode = bookingRoomService.savedBooking(roomId, bookingRequest);
            return ResponseEntity.ok("Room booked successfully. Your confirmation code: " + confirmationCode);
        } catch (InvalidBookingRequestException ibe) {
            return ResponseEntity.badRequest().body(ibe.getMessage());
        }
    }


    @GetMapping("/confirmation/{confirmationCode}")
    public ResponseEntity<?> getBookingByConfirmationCode(@PathVariable String confirmationCode){
        try {
            BookingRoom bookingRoom = bookingRoomService.findByBookingConfirmationCode(confirmationCode);
            BookingDTO bookingDTO = getBookingResponse(bookingRoom);
            return  ResponseEntity.ok(bookingDTO);
        } catch (ResourceNotFoundException ene) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ene.getMessage());

        }
    }

    @GetMapping("/user/{email}/bookings")
    public ResponseEntity<List<BookingDTO>> getBookingsByUserEmail(@PathVariable String email) {
        List<BookingRoom> bookings = bookingRoomService.findBookingsByUserEmail(email);
        List<BookingDTO> bookingDTOS = new ArrayList<>();
        for (BookingRoom booking : bookings) {
            BookingDTO bookingDTO = getBookingResponse(booking);
            bookingDTOS.add(bookingDTO);
        }
        return ResponseEntity.ok(bookingDTOS);
    }

    @DeleteMapping("/booking/{bookingId}/delete")
    public void cancelBooking(@PathVariable Long bookingId) {
        bookingRoomService.cancelBooking(bookingId);
    }
}
