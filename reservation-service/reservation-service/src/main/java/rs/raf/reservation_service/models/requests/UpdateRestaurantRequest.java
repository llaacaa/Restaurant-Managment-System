package rs.raf.reservation_service.models.requests;

public record UpdateRestaurantRequest (
    String name,
    String address,
    String description,
    Integer tableCount,
    String workingHours,
    String cuisineType
) {}
