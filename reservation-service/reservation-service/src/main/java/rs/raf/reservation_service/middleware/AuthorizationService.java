package rs.raf.reservation_service.middleware;

import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;
import rs.raf.reservation_service.models.DTOs.ClientDTO;
import rs.raf.reservation_service.models.DTOs.ManagerDTO;
import rs.raf.reservation_service.models.DTOs.RestaurantDTO;
import rs.raf.reservation_service.models.Restaurant;
import rs.raf.reservation_service.service.RestaurantService;
import rs.raf.reservation_service.service.api.ApiService;
import rs.raf.reservation_service.service.jwt.JwtTokenUtil;

import java.util.Optional;

@Service
public class AuthorizationService {

    private final JwtTokenUtil jwtTokenUtil;
    private final ApiService apiService;
    private final RestaurantService restaurantService;

    public AuthorizationService(JwtTokenUtil jwtTokenUtil, ApiService apiService, RestaurantService restaurantService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.apiService = apiService;
        this.restaurantService = restaurantService;
    }

    private record ValidationResponse(Long userId, String username, String role) {}

    private ValidationResponse getUserFromToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Authorization header is missing or invalid");
        }

        String token = authorizationHeader.substring(7);
        Claims claims = jwtTokenUtil.validateToken(token);
        String subject = claims.getSubject();
        String[] parts = subject.split(":");
        if (parts.length != 2) {
            throw new RuntimeException("Invalid token format");
        }

        Long userIdFromToken = Long.parseLong(parts[0]);
        String username = parts[1].split("-")[0];
        String role = parts[1].split("-")[1];

        if (!role.equals("MANAGER") && !role.equals("CLIENT")) {
            throw new UnauthorizedException("Invalid role");
        }

        return new ValidationResponse(userIdFromToken, username, role);
    }

    public ClientDTO validateClientAuthorization(String authorizationHeader) {

        ValidationResponse validationResponse = getUserFromToken(authorizationHeader);

        Long userIdFromToken = validationResponse.userId;
        String username = validationResponse.username;
        String role = validationResponse.role;

        if (!role.equals("CLIENT")) {
            throw new UnauthorizedException("NOT CLIENT");
        }

        ClientDTO clientDTO = apiService.fetchClientById(userIdFromToken);
        if (clientDTO == null) {
            throw new UnauthorizedException("NOT CLIENT");
        }

        return clientDTO;
    }

    public ManagerAuthDetails validateManagerAuthorization(String authorizationHeader, Long restaurantId) {

        ValidationResponse validationResponse = getUserFromToken(authorizationHeader);

        Long userIdFromToken = validationResponse.userId;
        String username = validationResponse.username;
        String role = validationResponse.role;

        if (!role.equals("MANAGER")) {
            throw new UnauthorizedException("NOT MANAGER");
        }

        ManagerDTO manager = apiService.fetchManagerById(userIdFromToken);
        if (manager == null) {
            throw new UnauthorizedException("NOT MANAGER");
        }
        RestaurantDTO managerRestaurant = restaurantService.getRestaurantByName(manager.restaurantName());
        if (managerRestaurant == null) {
            throw new UnauthorizedException("The restaurant you work in is not found in database");
        }

        if (!managerRestaurant.id().equals(restaurantId)) {
            throw new UnauthorizedException("Only managers of the restaurant are allowed to manage the restaurant");
        }

        Optional<Restaurant> restaurant = restaurantService.getRestaurantClass(managerRestaurant.id());
        if (!restaurant.isPresent()) {
            throw new RuntimeException("Restaurant not found");
        }

        return new ManagerAuthDetails(manager, restaurant.get());
    }
}
