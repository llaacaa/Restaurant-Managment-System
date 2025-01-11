package rs.raf.reservation_service.models.DTOs;

public record ReservationDTO(
        Long timeSlotTableId,
        Long userId
) {
}
