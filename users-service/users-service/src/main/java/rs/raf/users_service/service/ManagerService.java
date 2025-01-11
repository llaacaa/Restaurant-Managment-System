package rs.raf.users_service.service;

import org.springframework.stereotype.Service;
import rs.raf.users_service.models.DTOs.ManagerDTO;
import rs.raf.users_service.models.Managers;
import rs.raf.users_service.models.requests.ManagerRegistrationRequest;
import rs.raf.users_service.repository.ManagerRepository;
import rs.raf.users_service.service.mappers.ClassToDTO.ManagerDTOMapper;
import rs.raf.users_service.service.mappers.RequestToClass.RequestManagerMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ManagerService {
    private final ManagerRepository managerRepository;
    private final ManagerDTOMapper managerDTOMapper;
    private final RequestManagerMapper requestManagerMapper;

    public ManagerService(ManagerRepository managerRepository, RequestManagerMapper requestManagerMapper) {
        this.managerRepository = managerRepository;
        this.managerDTOMapper = new ManagerDTOMapper();
        this.requestManagerMapper = requestManagerMapper;
    }

    public List<ManagerDTO> getAllManagers() {
        return managerRepository.findAll()
                .stream()
                .map(managerDTOMapper)
                .collect(Collectors.toList());
    }

    public ManagerDTO getManagerById(Long id) {
        return managerRepository.findById(id).map(managerDTOMapper)
                .orElse(null);
    }

    public Optional<Managers> findManagerById(Long id) {
        return managerRepository.findById(id);
    }

    public ManagerDTO createManager(ManagerRegistrationRequest manager) {
        return managerDTOMapper.apply(managerRepository.save(requestManagerMapper.apply(manager)));
    }

    public void updateManager(Managers manager) {
        if (managerRepository.existsById(manager.getId())) {
            Managers managerToUpdate = managerRepository.findById(manager.getId()).get();
            managerToUpdate.setUsername(manager.getUsername());
            managerToUpdate.setPassword(manager.getPassword());
            managerToUpdate.setEmail(manager.getEmail());
            managerToUpdate.setDateOfBirth(manager.getDateOfBirth());
            managerToUpdate.setName(manager.getName());
            managerToUpdate.setSurname(manager.getSurname());
            managerToUpdate.setRestaurantName(manager.getRestaurantName());
            managerToUpdate.setDateOfEmployment(manager.getDateOfEmployment());
            managerRepository.save(managerToUpdate);
        } else {
            throw new IllegalArgumentException("Manager with id " + manager.getId() + " does not exist.");
        }
    }

    public void deleteManager(Long id) {
        if (managerRepository.existsById(id)) {
            managerRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Manager with id " + id + " does not exist.");
        }
    }
}
