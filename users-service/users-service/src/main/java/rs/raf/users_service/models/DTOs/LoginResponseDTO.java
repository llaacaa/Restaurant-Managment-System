package rs.raf.users_service.models.DTOs;

import rs.raf.users_service.models.ROLES;

public record LoginResponseDTO (
        String token,
        ROLES role,
        Long id
) {
}
