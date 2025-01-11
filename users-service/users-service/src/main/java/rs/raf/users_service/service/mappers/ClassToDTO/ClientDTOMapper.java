package rs.raf.users_service.service.mappers.ClassToDTO;

import org.springframework.stereotype.Service;
import rs.raf.users_service.models.Clients;
import rs.raf.users_service.models.DTOs.ClientDTO;

import java.util.function.Function;

@Service
public class ClientDTOMapper implements Function<Clients, ClientDTO> {
    @Override
    public ClientDTO apply(Clients client) {
        return new ClientDTO(
                client.getId(),
                client.getUsername(),
                client.getEmail(),
                client.getDateOfBirth(),
                client.getName(),
                client.getSurname(),
                client.getNumberOfReservations()
        );
    }
}
