package rs.raf.reservation_service.models.DTOs;

import rs.raf.reservation_service.models.ReservationStatus;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationDTO(
        Long id,
        Long timeSlotTableId,
        Long userId,
        LocalDate reservationDate,
        ReservationStatus status,
        String restaurantName,
        Integer numberOfSeats,
        LocalTime time,
        LocalDate date
) {
}
