package rs.raf.reservation_service.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import rs.raf.reservation_service.models.DTOs.ManagerDTO;
import rs.raf.reservation_service.models.DTOs.RestaurantDTO;
import rs.raf.reservation_service.models.Restaurant;
import rs.raf.reservation_service.models.requests.UpdateRestaurantRequest;
import rs.raf.reservation_service.repository.RestaurantRepository;
import rs.raf.reservation_service.service.api.ApiService;
import rs.raf.reservation_service.service.mappers.ClassToDTO.RestaurantDTOMapper;

import java.util.List;
import java.util.Optional;

@Service
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final RestaurantDTOMapper restaurantDTOMapper;
    private final ApiService apiService;

    public RestaurantService(RestaurantRepository restaurantRepository,
                             RestaurantDTOMapper restaurantDTOMapper, ApiService apiService) {
        this.restaurantRepository = restaurantRepository;
        this.restaurantDTOMapper = restaurantDTOMapper;
        this.apiService = apiService;
    }

    public  ResponseEntity<List<RestaurantDTO>> getAllRestaurants() {
        return new ResponseEntity<>(restaurantRepository.findAll().stream().map(restaurantDTOMapper).toList(), HttpStatus.OK);
    }

    public RestaurantDTO getRestaurantById(long id) {
        return restaurantRepository.findById(id).map(restaurantDTOMapper)
                .orElse(null);
    }

    public RestaurantDTO getRestaurantByName(String name) {
        return  restaurantRepository.findByName(name).map(restaurantDTOMapper).orElse(null);
    }


    public ResponseEntity<String> updateRestaurant(Long restaurantId, UpdateRestaurantRequest newRestaurantData, ManagerDTO manager) {
        if (restaurantRepository.existsById(restaurantId)) {
            Restaurant existingRestaurant = restaurantRepository.getById(restaurantId);

            if (!newRestaurantData.name().equals(existingRestaurant.getName())) {
                existingRestaurant.setName(newRestaurantData.name());
                var response = apiService.updateManagerRestaurantName(manager.id(), manager.username(), newRestaurantData.name());
                System.out.println("IDE " + response);
                if (!response.equals("Manager updated")) {
                    return new ResponseEntity<>("Restaurant update failed", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
            existingRestaurant.setAddress(newRestaurantData.address());
            existingRestaurant.setWorkingHours(newRestaurantData.workingHours());
            existingRestaurant.setDescription(newRestaurantData.description());
            existingRestaurant.setCuisineType(newRestaurantData.cuisineType());
            existingRestaurant.setTableCount(newRestaurantData.tableCount());
            restaurantRepository.save(existingRestaurant);
            return ResponseEntity.ok("Restaurant updated");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public Optional<Restaurant> getRestaurantClass(Long id) {
        return restaurantRepository.findById(id);
    }
}
