package rs.raf.reservation_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.raf.reservation_service.models.TimeSlotTables;

import java.util.List;

public interface TimeSlotTableRepository extends JpaRepository<TimeSlotTables, Long> {
    TimeSlotTables findByTimeSlotIdAndTableId(Long timeSlotId, Long tableId);

    List<TimeSlotTables> findByIsAvailable(Boolean isAvailable);
}
