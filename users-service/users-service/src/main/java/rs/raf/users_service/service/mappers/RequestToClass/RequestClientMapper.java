package rs.raf.users_service.service.mappers.RequestToClass;

import org.springframework.stereotype.Service;
import rs.raf.users_service.models.Clients;
import rs.raf.users_service.models.ROLES;
import rs.raf.users_service.models.requests.ClientRegistrationRequest;

import java.util.function.Function;

@Service
public class RequestClientMapper implements Function<ClientRegistrationRequest, Clients> {
    @Override
    public Clients apply(ClientRegistrationRequest clientRegistrationRequest) {
        return new Clients(
                clientRegistrationRequest.username(),
                clientRegistrationRequest.password(),
                clientRegistrationRequest.email(),
                clientRegistrationRequest.dateOfBirth(),
                clientRegistrationRequest.name(),
                clientRegistrationRequest.surname()
        );
    }
}
