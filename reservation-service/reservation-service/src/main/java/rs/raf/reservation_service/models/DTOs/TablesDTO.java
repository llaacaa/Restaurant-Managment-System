package rs.raf.reservation_service.models.DTOs;

import rs.raf.reservation_service.models.Location;
import rs.raf.reservation_service.models.Restaurant;
import rs.raf.reservation_service.models.Zone;

public record TablesDTO(
        Long id,
        Integer seats,
        Zone zone,
        Location location
) {
}
