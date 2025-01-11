package rs.raf.users_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.raf.users_service.models.Clients;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Clients, Long> {
    Optional<Clients> findByUsernameAndPassword(String username, String password);
}