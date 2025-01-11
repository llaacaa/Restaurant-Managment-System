package rs.raf.reservation_service.models.DTOs;

import rs.raf.reservation_service.models.Restaurant;

import java.time.LocalDate;
import java.time.LocalTime;

public record TimeSlotDTO (
    LocalDate date,
    LocalTime time,
    Restaurant restaurant
) {
}
