package rs.raf.reservation_service.models.DTOs;

import java.time.LocalDate;

public record ClientDTO(
        Long id,
        String username,
        String email,
        LocalDate dateOfBirth,
        String name,
        String surname,
        Integer numberOfReservations
) {

}
