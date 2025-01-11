package rs.raf.reservation_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf.reservation_service.middleware.AuthorizationService;
import rs.raf.reservation_service.middleware.ManagerAuthDetails;
import rs.raf.reservation_service.middleware.UnauthorizedException;
import rs.raf.reservation_service.models.*;
import rs.raf.reservation_service.models.DTOs.ClientDTO;
import rs.raf.reservation_service.service.ReservationService;
import rs.raf.reservation_service.service.TimeSlotTablesService;
import rs.raf.reservation_service.service.api.ApiService;

@RestController
@RequestMapping("/reservation")
public class ReservationController {
    private final ReservationService reservationService;
    private final TimeSlotTablesService timeSlotTablesService;
    private final AuthorizationService authorizationService;
    private final ApiService apiService;

    public ReservationController(ReservationService reservationService, TimeSlotTablesService timeSlotTablesService,
                                 AuthorizationService authorizationService, ApiService apiService) {
        this.reservationService = reservationService;
        this.timeSlotTablesService = timeSlotTablesService;
        this.authorizationService = authorizationService;
        this.apiService = apiService;
    }

    @PostMapping("/{timeSlotTableId}")
    public ResponseEntity<?> createReservation(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long timeSlotTableId) {
        try {
            ClientDTO client = authorizationService.validateClientAuthorization(authorizationHeader);
            TimeSlotTables timeSlotTables = timeSlotTablesService.findById(timeSlotTableId);
            if (timeSlotTables == null) {
                throw new RuntimeException("TimeSlotTable not found");
            }

            if (!timeSlotTables.getAvailable()) {
                throw new RuntimeException("TimeSlotTable is not available");
            }

            Reservation reservation = reservationService.createReservation(timeSlotTables, client.id());
            if (reservation == null) {
                throw new RuntimeException("Reservation creation failed");
            }

            timeSlotTables.setAvailable(false);
            timeSlotTables.setReservation(reservation);

            timeSlotTablesService.update(timeSlotTables);

            return new ResponseEntity<>("Reservation successfully created", HttpStatus.CREATED);

        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Internal error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{reservationId}/user-cancel")
    public ResponseEntity<?> cancelReservationUser(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long reservationId) {
        try {
            ClientDTO client = authorizationService.validateClientAuthorization(authorizationHeader);
            Reservation reservation = reservationService.findReservationById(reservationId);

            if (reservation == null) {
                throw new RuntimeException("Reservation not found");
            }

            if (!reservation.getUserId().equals(client.id())) {
                throw new RuntimeException("Reservation is not owner of user");
            }

            if (!reservation.getStatus().equals(ReservationStatus.CONFIRMED)) {
                throw new RuntimeException("Reservation is already cancelled");
            }

            TimeSlotTables timeSlotTables = reservation.getTimeSlotTables();

            reservation.setStatus(ReservationStatus.CANCELLED_BY_USER);

            timeSlotTables.setReservation(null);
            timeSlotTables.setAvailable(true);

            reservationService.updateReservation(reservation);
            timeSlotTablesService.update(timeSlotTables);

            apiService.updateUserReservation(client.id(), "decrement");

            return new ResponseEntity<>("Reservation cancelled", HttpStatus.OK);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Internal error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{reservationId}/manager-cancel")
    public ResponseEntity<?> cancelReservationManager(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long reservationId) {
        try {
            Reservation reservation = reservationService.findReservationById(reservationId);
            if (reservation == null) {
                throw new RuntimeException("Reservation not found");
            }

            if (!reservation.getStatus().equals(ReservationStatus.CONFIRMED)) {
                throw new RuntimeException("Reservation is already cancelled");
            }

            TimeSlotTables timeSlotTables = reservation.getTimeSlotTables();

            ManagerAuthDetails authDetails = authorizationService.validateManagerAuthorization(
                    authorizationHeader, timeSlotTables.getTimeSlot().getRestaurant().getId());


            reservation.setStatus(ReservationStatus.CANCELLED_BY_MANAGER);
            // TODO NOTIFY USER

            reservationService.updateReservation(reservation);

            return new ResponseEntity<>("Reservation cancelled", HttpStatus.OK);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Internal error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
