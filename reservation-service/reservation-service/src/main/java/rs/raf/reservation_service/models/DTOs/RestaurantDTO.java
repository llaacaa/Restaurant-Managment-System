package rs.raf.reservation_service.models.DTOs;

public record RestaurantDTO (
        Long id,
        String name,
        String address,
        String description,
        Integer tableCount,
        String workingHours,
        String cuisineType
) {
}
