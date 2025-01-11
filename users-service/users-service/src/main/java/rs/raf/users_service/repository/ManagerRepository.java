package rs.raf.users_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.raf.users_service.models.Managers;

import java.util.Optional;

@Repository
public interface ManagerRepository extends JpaRepository<Managers, Long> {
    Optional<Managers> findByUsernameAndPassword(String username, String password);
}
