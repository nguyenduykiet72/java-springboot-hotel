package com.example.javahotel.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
    private int statusCode;
    private String message;
    private String token;
    private String role;
    private String bookingConfirmationCode;
    private String expirationTime;
    private UserDTO userDTO;
    private RoomDTO roomDTO;
    private BookingDTO bookingDTO;
    private List<UserDTO> userDTOList;
    private List<RoomDTO> roomDTOList;
    private List<BookingDTO> bookingDTOList;
}
