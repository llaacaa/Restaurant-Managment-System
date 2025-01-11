package rs.raf.users_service.service.mappers.RequestToClass;

import org.springframework.stereotype.Service;
import rs.raf.users_service.models.Managers;
import rs.raf.users_service.models.ROLES;
import rs.raf.users_service.models.requests.ManagerRegistrationRequest;

import java.util.function.Function;

@Service
public class RequestManagerMapper implements Function<ManagerRegistrationRequest, Managers> {
    @Override
    public Managers apply(ManagerRegistrationRequest managerRegistrationRequest) {
        return new Managers(
            managerRegistrationRequest.username(),
            managerRegistrationRequest.password(),
            managerRegistrationRequest.email(),
            managerRegistrationRequest.dateOfBirth(),
            managerRegistrationRequest.name(),
            managerRegistrationRequest.surname(),
            managerRegistrationRequest.restaurantName(),
            managerRegistrationRequest.dateOfEmployment()
        );
    }
}
