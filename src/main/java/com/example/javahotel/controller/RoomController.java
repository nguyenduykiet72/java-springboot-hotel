package com.example.javahotel.controller;

import com.example.javahotel.dto.response.BookingDTO;
import com.example.javahotel.dto.response.RoomDTO;
import com.example.javahotel.entity.BookingRoom;
import com.example.javahotel.entity.Room;
import com.example.javahotel.exception.PhotoRetrievalException;
import com.example.javahotel.exception.ResourceNotFoundException;
import com.example.javahotel.service.BookingRoomService;
import com.example.javahotel.service.RoomService;
import com.example.javahotel.service.impl.BookingRoomServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;
    private final BookingRoomService bookingRoomService;

    @CrossOrigin
    @PostMapping("/add/new-room")
    public ResponseEntity<RoomDTO> addNewRoom(
            @RequestParam("photo") MultipartFile photo,
            @RequestParam("roomType") String roomType,
            @RequestParam("roomPrice") BigDecimal roomPrice) throws SQLException, IOException {
        Room savedRoom = roomService.addNewRoom(photo, roomType, roomPrice);
        RoomDTO roomDTO = new RoomDTO(savedRoom.getId(), savedRoom.getRoomType(), savedRoom.getRoomPrice());
        return ResponseEntity.ok(roomDTO);
    }

    @CrossOrigin
    @GetMapping("/room/types")
    public List<String> getRoomTypes() {
        return roomService.getAllRoomTypes();
    }

    @CrossOrigin
    @GetMapping("/all-rooms")
    public ResponseEntity<List<RoomDTO>> getAllRooms() throws SQLException {
        List<Room> rooms = roomService.getAllRooms();
        List<RoomDTO> roomDTOS = new ArrayList<>();
        for (Room room : rooms) {
            byte[] photoBytes = roomService.getRoomPhotoByRoomId(room.getId());
            if (photoBytes != null && photoBytes.length > 0) {
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
        if (photoBlob != null) {
            try {
                photoBytes = photoBlob.getBytes(1, (int) photoBlob.length());
            } catch (SQLException e) {
                throw new PhotoRetrievalException("Error retrieving photo");
            }
        }
        return new RoomDTO(room.getId(), room.getRoomType(), room.getRoomPrice(), room.isBooked(), photoBytes, bookingInfo);
    }

    @DeleteMapping("/delete/room/{roomId}")
    @CrossOrigin
    public ResponseEntity<Void> deleteRoom(@PathVariable Long roomId) {
        roomService.deleteRoom(roomId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/update/{roomId}")
    @CrossOrigin
    public ResponseEntity<RoomDTO> updateRoom(@PathVariable Long roomId,
                                              @RequestParam(required = false) String roomType,
                                              @RequestParam(required = false) BigDecimal roomPrice,
                                              @RequestParam(required = false) MultipartFile photo) throws IOException, SQLException {
        byte[] photoBytes = photo != null && !photo.isEmpty() ?
                photo.getBytes() : roomService.getRoomPhotoByRoomId(roomId);
        Blob photoBlob = photoBytes != null && photoBytes.length > 0 ? new SerialBlob(photoBytes) : null;
        Room theRoom = roomService.updateRoom(roomId,roomType,roomPrice, photoBytes);
        theRoom.setPhoto(photoBlob);
        RoomDTO roomDTO = getRoomResponse(theRoom);
        return ResponseEntity.ok(roomDTO);
    }

    @GetMapping("/room/{roomId}")
    @CrossOrigin
    public ResponseEntity<Optional<RoomDTO>> getRoomById(@PathVariable Long roomId) {
        Optional<Room> theRoom = roomService.getRoomById(roomId);
        return theRoom.map(room -> {
            RoomDTO roomDTO = getRoomResponse(room);
            return ResponseEntity.ok(Optional.of(roomDTO));
        }).orElseThrow(() -> new ResourceNotFoundException("Room not found"));
    }
}
