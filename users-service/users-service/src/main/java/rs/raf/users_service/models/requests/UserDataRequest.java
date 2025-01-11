package rs.raf.users_service.models.requests;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.Optional;

public record UserDataRequest(
        @NotNull(message = "Id is required")
        Long id,

        @NotBlank(message = "Username is required")
        String username,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        Optional<@NotBlank(message = "Password cannot be blank") String> password,

        @NotNull(message = "Date of birth is required")
        LocalDate dateOfBirth,

        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Surname is required")
        String surname,

        Optional<@NotBlank(message = "Restaurant name cannot be blank") String> restaurantName,

        Optional<@NotNull(message = "Date of employment cannot be null") LocalDate> dateOfEmployment
) {
}

