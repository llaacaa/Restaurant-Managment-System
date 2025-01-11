package rs.raf.reservation_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.raf.reservation_service.models.Tables;

@Repository
public interface TablesRepository extends JpaRepository<Tables, Long> {
}

