package rs.raf.users_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.raf.users_service.models.Admins;

@Repository
public interface AdminRepository extends JpaRepository<Admins, Long> {}
