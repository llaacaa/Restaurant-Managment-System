package rs.raf.users_service.models.DTOs;

import java.time.LocalDate;

public record ManagerDTO (
        Long id,
        String username,
        String email,
        LocalDate dateOfBirth,
        String name,
        String surname,
        String restaurantName,
        LocalDate dateOfEmployment
){
}