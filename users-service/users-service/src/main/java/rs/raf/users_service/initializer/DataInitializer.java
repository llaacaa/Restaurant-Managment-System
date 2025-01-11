package rs.raf.users_service.initializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import rs.raf.users_service.models.Admins;
import rs.raf.users_service.models.Clients;
import rs.raf.users_service.models.Managers;
import rs.raf.users_service.models.Users;
import rs.raf.users_service.repository.AdminRepository;
import rs.raf.users_service.repository.ClientRepository;
import rs.raf.users_service.repository.ManagerRepository;

import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {
    private final ClientRepository clientRepository;
    private final AdminRepository adminRepository;
    private final ManagerRepository managerRepository;

    @Autowired
    public DataInitializer(ClientRepository clientRepository, AdminRepository adminRepository, ManagerRepository managerRepository) {
        this.clientRepository = clientRepository;
        this.adminRepository = adminRepository;
        this.managerRepository = managerRepository;
    }


    @Override
    public void run(String... args) {
        if (clientRepository.findAll().isEmpty()) {
            // Using the constructor for convenience
            Users client = new Clients(
                    "client1",
                    "password123",
                    "client1@example.com",
                    LocalDate.of(1995, 8, 20),
                    "John",
                    "Doe"
            );

            clientRepository.save((Clients) client);

            System.out.println("Client created: " + client.getUsername());

            Users admin = new Admins(
                    "lacaAdmin",
                    "Lacke123",
                    "lackeAdmin@admin.com",
                    LocalDate.of(2004,2,2),
                    "lacko",
                    "luda"
            );

            adminRepository.save((Admins) admin);

            System.out.println("Admin created: " + admin.getUsername());

            Users manager = new Managers(
                    "lacaMenadzer",
                    "Lacke123",
                    "lackeMen@men.com",
                    LocalDate.of(1995, 8, 20),
                    "lackomen",
                    "menag",
                    "Lacin restoran",
                    LocalDate.now()
            );

            managerRepository.save((Managers) manager);

            System.out.println("Manager created: " + manager.getUsername());

        } else {
            System.out.println("Client already exists.");
        }
    }
}
