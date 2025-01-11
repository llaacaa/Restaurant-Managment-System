package rs.raf.reservation_service.service.mappers.ClassToDTO;

import org.springframework.stereotype.Service;
import rs.raf.reservation_service.models.DTOs.TimeSlotDTO;
import rs.raf.reservation_service.models.TimeSlot;

import java.util.function.Function;

@Service
public class TimeSlotDtoMapper implements Function<TimeSlot, TimeSlotDTO> {
    @Override
    public TimeSlotDTO apply(TimeSlot timeSlot) {
        return new TimeSlotDTO(
                timeSlot.getDate(),
                timeSlot.getTime(),
                timeSlot.getRestaurant()
        );
    }
}
