package rs.raf.reservation_service.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import rs.raf.reservation_service.models.DTOs.ReservationDTO;
import rs.raf.reservation_service.models.Reservation;
import rs.raf.reservation_service.models.ReservationStatus;
import rs.raf.reservation_service.models.TimeSlotTables;
import rs.raf.reservation_service.repository.ReservationRepository;
import rs.raf.reservation_service.service.api.ApiService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ApiService apiService;

    public ReservationService(ReservationRepository reservationRepository, ApiService apiService) {
        this.reservationRepository = reservationRepository;
        this.apiService = apiService;
    }

    public List<ReservationDTO> getAllReservations() {
        return reservationRepository.findAll().stream().map(reservation -> new ReservationDTO(
                reservation.getId(),
                reservation.getTimeSlotTables().getId(),
                reservation.getUserId(),
                reservation.getReservationDate(),
                reservation.getStatus(),
                reservation.getTimeSlotTables().getTable().getRestaurant().getName(),
                reservation.getTimeSlotTables().getTable().getSeats(),
                reservation.getTimeSlotTables().getTimeSlot().getTime(),
                reservation.getTimeSlotTables().getTimeSlot().getDate()
        )).toList();
    }

    public Reservation findReservationById(Long id) {
        return reservationRepository.findById(id).orElse(null);
    }

    public void updateReservation(Reservation reservation) {
        reservationRepository.save(reservation);
    }

    @Transactional
    public Reservation createReservation(TimeSlotTables timeSlotTableId, Long userId) {
        Reservation reservation = new Reservation();
        reservation.setTimeSlotTables(timeSlotTableId);
        reservation.setUserId(userId);
        reservation.setReservationDate(LocalDate.now());
        reservation.setStatus(ReservationStatus.CONFIRMED);

        String response = apiService.updateUserReservation(userId, "increment");

        if (!response.equals("Client updated successfully")) {
            throw new RuntimeException("Incorrect response received from user service");
        }

        reservationRepository.save(reservation);

        return reservation;
    }
}
