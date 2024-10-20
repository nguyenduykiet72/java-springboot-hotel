package com.example.javahotel.repository;

import com.example.javahotel.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Long> {

    List<Booking> findByRoomId(Long roomId);


    List<Booking> findByBookingConfirmationCode(String confirmationCode);


    List<Booking> findByUserId(Long userId);
}
