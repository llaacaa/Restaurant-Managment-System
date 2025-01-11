package rs.raf.reservation_service.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import rs.raf.reservation_service.models.Reservation;
import rs.raf.reservation_service.models.ReservationStatus;
import rs.raf.reservation_service.models.TimeSlotTables;
import rs.raf.reservation_service.repository.ReservationRepository;
import rs.raf.reservation_service.service.api.ApiService;

import java.time.LocalDate;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final ApiService apiService;

    public ReservationService(ReservationRepository reservationRepository, ApiService apiService) {
        this.reservationRepository = reservationRepository;
        this.apiService = apiService;
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
