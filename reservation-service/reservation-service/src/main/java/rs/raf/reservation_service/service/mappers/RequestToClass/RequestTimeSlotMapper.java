package rs.raf.reservation_service.service.mappers.RequestToClass;

import org.springframework.stereotype.Service;
import rs.raf.reservation_service.models.DTOs.TimeSlotDTO;
import rs.raf.reservation_service.models.TimeSlot;

import java.util.function.Function;

@Service
public class RequestTimeSlotMapper implements Function<TimeSlotDTO, TimeSlot> {


    @Override
    public TimeSlot apply(TimeSlotDTO timeSlotDTO) {
         TimeSlot timeSlot = new TimeSlot();
         timeSlot.setTime(timeSlotDTO.time());
         timeSlot.setDate(timeSlotDTO.date());
         timeSlot.setRestaurant(timeSlotDTO.restaurant());
         return timeSlot;
    }
}
