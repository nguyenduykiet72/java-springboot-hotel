package com.example.javahotel.repository;

import com.example.javahotel.entity.BookingRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRoomRepository extends JpaRepository<BookingRoom,Long> {

    List<BookingRoom> findByRoomId(Long roomId);

    Optional<BookingRoom> findByBookingConfirmationCode(String confirmationCode);

    List<BookingRoom> findByGuestEmail(String email);
}
