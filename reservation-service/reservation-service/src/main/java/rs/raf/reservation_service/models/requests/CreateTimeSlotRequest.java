package rs.raf.reservation_service.models.requests;

import java.time.LocalDate;
import java.time.LocalTime;

public record CreateTimeSlotRequest
        (
                LocalDate date,
                LocalTime time,
                Long restaurantId
        )
{
}
