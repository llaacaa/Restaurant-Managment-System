package rs.raf.reservation_service.models.requests;

import rs.raf.reservation_service.models.Location;
import rs.raf.reservation_service.models.Zone;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

public record TableFiltersRequest(
        Optional<Long> restaurantId,
        Optional<String> typeOfCuisine,
        Optional<Zone> zone,
        Optional<Location> location,
        Optional<Integer> numberOfSeats,
        Optional<LocalDate> date,
        Optional<LocalTime> time
) {
}
