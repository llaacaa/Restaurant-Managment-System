package rs.raf.reservation_service.service.api;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import rs.raf.reservation_service.models.DTOs.ClientDTO;
import rs.raf.reservation_service.models.DTOs.ManagerDTO;
import rs.raf.reservation_service.service.jwt.JwtTokenUtil;

@Service
public class ApiService {

    private final WebClient webClient;
    private final JwtTokenUtil jwtTokenUtil;

    public ApiService(WebClient webClient, JwtTokenUtil jwtTokenUtil) {
        this.webClient = webClient;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public ManagerDTO fetchManagerById(Long id) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/manager/{managerId}")
                        .build(id))
                .retrieve()
                .bodyToMono(ManagerDTO.class)
                .block();
    }

    public ClientDTO fetchClientById(Long id) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/client/{clientId}")
                        .build(id))
                .retrieve()
                .bodyToMono(ClientDTO.class)
                .block();
    }

    public String updateManagerRestaurantName(Long id, String username, String newRestaurantName) {
        var token = jwtTokenUtil.generateToken(id, username);
        return webClient
            .post()
            .uri(uriBuilder -> uriBuilder
                    .path("/manager/restaurant/{newRestaurantName}")
                    .build(newRestaurantName))
            .header("Authorization", "Bearer " + token)
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }

    public String updateUserReservation(Long userId, String operation) {
        var token = jwtTokenUtil.generateToken(userId, "n/a");
        return webClient
                .put()
                .uri(uriBuilder -> uriBuilder
                        .path("/client/update-reservation")
                        .queryParam("operation", operation)
                        .build(operation))
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
