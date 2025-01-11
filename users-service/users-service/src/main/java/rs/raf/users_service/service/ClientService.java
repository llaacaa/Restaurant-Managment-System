package rs.raf.users_service.service;

import org.springframework.stereotype.Service;
import rs.raf.users_service.models.Clients;
import rs.raf.users_service.models.DTOs.ClientDTO;
import rs.raf.users_service.models.requests.ClientRegistrationRequest;
import rs.raf.users_service.repository.ClientRepository;
import rs.raf.users_service.service.mappers.ClassToDTO.ClientDTOMapper;
import rs.raf.users_service.service.mappers.RequestToClass.RequestClientMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientService {
    private final ClientRepository clientRepository;
    private final ClientDTOMapper clientDTOMapper;
    private final RequestClientMapper requestClientMapper;

    public ClientService(ClientRepository clientRepository, ClientDTOMapper clientDTOMapper, RequestClientMapper requestClientMapper) {
        this.clientRepository = clientRepository;
        this.clientDTOMapper = clientDTOMapper;
        this.requestClientMapper = requestClientMapper;
    }

    public List<ClientDTO> getAllClients() {
        return clientRepository.findAll()
                .stream()
                .map(clientDTOMapper)
                .collect(Collectors.toList());
    }

    public ClientDTO getClientById(Long id) {
        return clientRepository.findById(id).map(clientDTOMapper)
                .orElse(null);
    }

    public ClientDTO createClient(ClientRegistrationRequest client) {
        return clientDTOMapper.apply(clientRepository.save(requestClientMapper.apply(client)));
    }


    public void updateClient(Clients client) {
        if (clientRepository.existsById(client.getId())) {
            Clients clientToUpdate = clientRepository.findById(client.getId()).get();
            clientToUpdate.setUsername(client.getUsername());
            clientToUpdate.setPassword(client.getPassword());
            clientToUpdate.setEmail(client.getEmail());
            clientToUpdate.setDateOfBirth(client.getDateOfBirth());
            clientToUpdate.setName(client.getName());
            clientToUpdate.setSurname(client.getSurname());
            clientRepository.save(clientToUpdate);
        } else {
            throw new IllegalArgumentException("Client with id " + client.getId() + " does not exist.");
        }
    }

    public void deleteClient(Long id) {
        if (clientRepository.existsById(id)) {
            clientRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Client with id " + id + " does not exist.");
        }
    }

    public void updateReservationNumber(Long id, String operation) {
        if (clientRepository.existsById(id)) {
            Clients clientToUpdate = clientRepository.findById(id).get();
            if (operation.equals("increment")) {
                clientToUpdate.setNumberOfReservations(clientToUpdate.getNumberOfReservations() + 1);
            } else if (operation.equals("decrement")) {
                clientToUpdate.setNumberOfReservations(clientToUpdate.getNumberOfReservations() - 1);
            } else {
                throw new IllegalArgumentException("Invalid operation " + operation);
            }
            clientRepository.save(clientToUpdate);
        } else {
            throw new IllegalArgumentException("Client with id " + id + " does not exist.");
        }
    }
}
