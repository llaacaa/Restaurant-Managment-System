package rs.raf.reservation_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf.reservation_service.middleware.AuthorizationService;
import rs.raf.reservation_service.middleware.ManagerAuthDetails;
import rs.raf.reservation_service.middleware.UnauthorizedException;
import rs.raf.reservation_service.models.DTOs.RestaurantDTO;
import rs.raf.reservation_service.models.Restaurant;
import rs.raf.reservation_service.models.requests.UpdateRestaurantRequest;
import rs.raf.reservation_service.service.RestaurantService;

import java.util.List;

@RestController
@RequestMapping("/restaurant")
public class RestaurantController {
    private final RestaurantService restaurantService;
    private final AuthorizationService authorizationService;

    public RestaurantController(RestaurantService restaurantService, AuthorizationService authorizationService) {
        this.restaurantService = restaurantService;
        this.authorizationService = authorizationService;
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<String> updateRestaurant(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long id,
            @RequestBody UpdateRestaurantRequest restaurantData) {
        try {
            ManagerAuthDetails authDetails = authorizationService.validateManagerAuthorization(
                    authorizationHeader, id);
            return restaurantService.updateRestaurant(id, restaurantData, authDetails.getManager());
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Internal error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<RestaurantDTO>> getRestaurants() {
        return restaurantService.getAllRestaurants();
    }
}
