package rs.raf.users_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.raf.users_service.models.Users;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUsernameAndPassword(String username, String password);
    Optional<Users> findByUsername(String username);

    Users findByEmail(String email);
}
