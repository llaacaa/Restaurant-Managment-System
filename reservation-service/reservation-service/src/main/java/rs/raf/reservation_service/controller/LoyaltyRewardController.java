package rs.raf.reservation_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf.reservation_service.middleware.AuthorizationService;
import rs.raf.reservation_service.middleware.ManagerAuthDetails;
import rs.raf.reservation_service.middleware.UnauthorizedException;
import rs.raf.reservation_service.models.DTOs.LoyaltyRewardDTO;
import rs.raf.reservation_service.models.LoyaltyReward;
import rs.raf.reservation_service.models.Restaurant;
import rs.raf.reservation_service.service.LoyaltyRewardService;

@RestController
@RequestMapping("/loyalty-reward")
public class LoyaltyRewardController {
    private final LoyaltyRewardService loyaltyRewardService;
    private final AuthorizationService authorizationService;

    public LoyaltyRewardController(LoyaltyRewardService loyaltyRewardService, AuthorizationService authorizationService) {
        this.loyaltyRewardService = loyaltyRewardService;
        this.authorizationService = authorizationService;
    }

    @PostMapping()
    public ResponseEntity<String> loyaltyReward(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody LoyaltyRewardDTO loyaltyRewardDTO) {
        try {
            ManagerAuthDetails authDetails = authorizationService.validateManagerAuthorization(authorizationHeader, loyaltyRewardDTO.restaurantId());
            Restaurant restaurant = authDetails.getRestaurant();

            LoyaltyReward loyaltyReward = loyaltyRewardService.findLoyaltyRewardByCondition(loyaltyRewardDTO.condition());
            if (loyaltyReward != null) {
                throw new UnauthorizedException("Loyalty reward with same condition already exists");
            }

            loyaltyRewardService.createLoyaltyReward(loyaltyRewardDTO, restaurant);


            return new ResponseEntity<>("Loyalty reward created", HttpStatus.CREATED);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Internal error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
