package rs.raf.users_service.models.requests;

import java.time.LocalDate;

public record ClientRegistrationRequest (
        String username,
        String email,
        String password,
        LocalDate dateOfBirth,
        String name,
        String surname
) {
}
