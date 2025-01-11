package rs.raf.reservation_service.models.DTOs;

import java.time.LocalDate;
import java.time.LocalTime;

public record TimeSlotDTOt(
        Long id,
        LocalDate date,
        LocalTime time
) {
}
