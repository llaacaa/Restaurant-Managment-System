package rs.raf.reservation_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf.reservation_service.middleware.AuthorizationService;
import rs.raf.reservation_service.middleware.ManagerAuthDetails;
import rs.raf.reservation_service.middleware.UnauthorizedException;
import rs.raf.reservation_service.models.DTOs.TimeSlotDTO;
import rs.raf.reservation_service.models.TimeSlot;
import rs.raf.reservation_service.models.requests.CreateTimeSlotRequest;
import rs.raf.reservation_service.service.TimeSlotService;


@RestController
@RequestMapping("/time-slot")
public class TimeSlotController {
    private final TimeSlotService timeSlotService;
    private final AuthorizationService authorizationService;

    public TimeSlotController(TimeSlotService timeSlotService, AuthorizationService authorizationService) {
        this.timeSlotService = timeSlotService;
        this.authorizationService = authorizationService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createTimeSlot(
            @RequestBody CreateTimeSlotRequest timeSlot,
            @RequestHeader("Authorization") String authorizationHeader) {
        try {
            ManagerAuthDetails authDetails = authorizationService.validateManagerAuthorization(
                    authorizationHeader, timeSlot.restaurantId());

            TimeSlot timeSlotNew = timeSlotService.createTimeSlot(
                    new TimeSlotDTO(timeSlot.date(), timeSlot.time(), authDetails.getRestaurant()));
            timeSlotService.addTimeSlot(timeSlotNew);

            return new ResponseEntity<>("Time slot created", HttpStatus.CREATED);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Internal error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
