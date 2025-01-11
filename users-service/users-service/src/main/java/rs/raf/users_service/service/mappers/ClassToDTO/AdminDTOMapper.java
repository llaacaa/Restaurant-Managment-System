package rs.raf.users_service.service.mappers.ClassToDTO;

import org.springframework.stereotype.Service;
import rs.raf.users_service.models.Admins;
import rs.raf.users_service.models.DTOs.AdminDTO;

import java.util.function.Function;

@Service
public class AdminDTOMapper implements Function<Admins, AdminDTO> {
    @Override
    public AdminDTO apply(Admins admin) {
        return new AdminDTO(
                admin.getId(),
                admin.getUsername(),
                admin.getEmail(),
                admin.getDateOfBirth(),
                admin.getName(),
                admin.getSurname()
        );
    }
}
