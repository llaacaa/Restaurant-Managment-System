package rs.raf.users_service.models.requests;

public record UserLoginRequest(
        String username,
        String password
) {
}
