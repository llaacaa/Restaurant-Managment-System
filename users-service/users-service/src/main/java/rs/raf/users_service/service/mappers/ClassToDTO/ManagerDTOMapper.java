package rs.raf.users_service.service.mappers.ClassToDTO;

import org.springframework.stereotype.Service;
import rs.raf.users_service.models.DTOs.ManagerDTO;
import rs.raf.users_service.models.Managers;

import java.util.function.Function;

@Service
public class ManagerDTOMapper implements Function<Managers, ManagerDTO> {
    @Override
    public ManagerDTO apply(Managers manager) {
        return new ManagerDTO(
                manager.getId(),
                manager.getUsername(),
                manager.getEmail(),
                manager.getDateOfBirth(),
                manager.getName(),
                manager.getSurname(),
                manager.getRestaurantName(),
                manager.getDateOfEmployment()
        );
    }
}
