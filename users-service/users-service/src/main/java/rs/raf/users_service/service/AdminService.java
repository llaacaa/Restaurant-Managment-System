package rs.raf.users_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.raf.users_service.models.Admins;
import rs.raf.users_service.models.DTOs.AdminDTO;
import rs.raf.users_service.repository.AdminRepository;
import rs.raf.users_service.service.mappers.ClassToDTO.AdminDTOMapper;

import java.util.Optional;

@Service
public class AdminService {
    private final AdminRepository adminRepository;
    private final AdminDTOMapper adminDTOMapper;

    public AdminService(AdminRepository adminRepository, AdminDTOMapper adminDTOMapper) {
        this.adminRepository = adminRepository;
        this.adminDTOMapper = adminDTOMapper;
    }

    public AdminDTO getAdminById(Long id) {
        return adminRepository.findById(id).map(adminDTOMapper)
                .orElse(null);
    }

    public void updateAdmin(Admins admin) {
        if (adminRepository.existsById(admin.getId())) {
            Admins existingAdmin = adminRepository.findById(admin.getId()).get();
            existingAdmin.setUsername(admin.getUsername());
            existingAdmin.setEmail(admin.getEmail());
            existingAdmin.setDateOfBirth(admin.getDateOfBirth());
            existingAdmin.setName(admin.getName());
            existingAdmin.setSurname(admin.getSurname());
            adminRepository.save(existingAdmin);
        } else {
            throw new IllegalArgumentException("Admin with id " + admin.getId() + " does not exist.");
        }
    }

}
