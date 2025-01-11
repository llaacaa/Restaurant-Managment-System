package rs.raf.reservation_service.service.mappers.ClassToDTO;

import org.springframework.stereotype.Service;
import rs.raf.reservation_service.models.DTOs.RestaurantDTO;
import rs.raf.reservation_service.models.Restaurant;

import java.util.function.Function;

@Service
public class RestaurantDTOMapper implements Function<Restaurant, RestaurantDTO> {
    @Override
    public RestaurantDTO apply(Restaurant restaurant) {
        return new RestaurantDTO(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getAddress(),
                restaurant.getDescription(),
                restaurant.getTableCount(),
                restaurant.getWorkingHours(),
                restaurant.getCuisineType()
        );
    }
}
