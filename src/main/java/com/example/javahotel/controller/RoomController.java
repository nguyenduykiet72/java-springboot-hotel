package com.example.javahotel.controller;

import com.example.javahotel.dto.response.BookingDTO;
import com.example.javahotel.dto.response.RoomDTO;
import com.example.javahotel.entity.BookingRoom;
import com.example.javahotel.entity.Room;
import com.example.javahotel.exception.PhotoRetrievalException;
import com.example.javahotel.service.RoomService;
import com.example.javahotel.service.impl.BookingRoomServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;
    private final BookingRoomServiceImpl bookingRoomService;

    @CrossOrigin
    @PostMapping("/add/new-room")
    public ResponseEntity<RoomDTO> addNewRoom(
            @RequestParam("photo") MultipartFile photo,
            @RequestParam("roomType") String roomType,
            @RequestParam("roomPrice") BigDecimal roomPrice) throws SQLException, IOException {
        Room savedRoom = roomService.addNewRoom(photo, roomType, roomPrice);
        RoomDTO roomDTO = new RoomDTO(savedRoom.getId(), savedRoom.getRoomType(),savedRoom.getRoomPrice());
        return ResponseEntity.ok(roomDTO);
    }

    @CrossOrigin
    @GetMapping("/room/types")
    public List<String> getRoomTypes(){
        return roomService.getAllRoomTypes();
    }

    public ResponseEntity<List<RoomDTO>> getAllRooms() throws SQLException {
        List<Room> rooms = roomService.getAllRooms();
        List<RoomDTO> roomDTOS = new ArrayList<>();
        for (Room room : rooms) {
            byte[] photoBytes = roomService.getRoomPhotoByRoomId(room.getId());
            if(photoBytes != null && photoBytes.length > 0){
                String base64Photo = Base64.encodeBase64String(photoBytes);
                RoomDTO roomDTO = getRoomResponse(room);
                roomDTO.setPhoto(base64Photo);
                roomDTOS.add(roomDTO);
            }
        }
        return ResponseEntity.ok(roomDTOS);
    }

    private List<BookingRoom> getAllBookingsByRoomId(Long roomId) {
        return bookingRoomService.getAllBookingsByRoomId(roomId);
    }

    private RoomDTO getRoomResponse(Room room) {
        List<BookingRoom> bookings = getAllBookingsByRoomId(room.getId());
        List<BookingDTO> bookingInfo = bookings
                .stream()
                .map(booking -> new BookingDTO(
                        booking.getBookingId(),
                        booking.getCheckInDate(),
                        booking.getCheckOutDate(),
                        booking.getBookingConfirmationCode()
                )).toList();
        byte[] photoBytes = null;
        Blob photoBlob = room.getPhoto();
        if(photoBlob != null) {
            try {
                photoBytes = photoBlob.getBytes(1,(int)photoBlob.length());
            } catch (SQLException e){
                throw new PhotoRetrievalException("Error retieving photo");
            }
        }
        return new RoomDTO(room.getId(),room.getRoomType(), room.getRoomPrice(), room.isBooked(), photoBytes,bookingInfo);
    }


}
