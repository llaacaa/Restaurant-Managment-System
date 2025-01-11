package rs.raf.reservation_service.models.requests;

import rs.raf.reservation_service.models.Location;
import rs.raf.reservation_service.models.Zone;

import java.util.Optional;

public record ManageTablesRequest(
        Long tableId,
        Integer seats,
        Zone zone,
        Location location,
        Optional<Long> timeSlotId,
        Optional<Boolean> availability
) {
}
