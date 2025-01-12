package rs.raf.reservation_service.service;

import org.springframework.stereotype.Service;
import rs.raf.reservation_service.models.DTOs.TablesDTOt;
import rs.raf.reservation_service.models.DTOs.TimeSlotDTOt;
import rs.raf.reservation_service.models.DTOs.TimeSlotTableDTO;
import rs.raf.reservation_service.models.TimeSlotTables;
import rs.raf.reservation_service.models.requests.TableFiltersRequest;
import rs.raf.reservation_service.repository.TimeSlotTableRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TimeSlotTablesService {
    private final TimeSlotTableRepository timeSlotTableRepository;

    public TimeSlotTablesService(TimeSlotTableRepository timeSlotTableRepository) {
        this.timeSlotTableRepository = timeSlotTableRepository;
    }

    public TimeSlotTables findById(Long id) {
        return timeSlotTableRepository.findById(id).orElse(null);
    }

    public TimeSlotTables findByTimeSlotAndTable(Long timeSlot, Long table) {
        return timeSlotTableRepository.findByTimeSlotIdAndTableId(timeSlot, table);
    }

    public void changeAvailability(TimeSlotTables timeSlotTables) {
        timeSlotTableRepository.save(timeSlotTables);
    }

    public List<TimeSlotTableDTO> findAll(Optional<TableFiltersRequest> filterOptional) {
        List<TimeSlotTables> timeSlotTables = timeSlotTableRepository.findByIsAvailable(true);

        if (filterOptional.isPresent()) {
            TableFiltersRequest filters = filterOptional.get();

            if (filters.date().isPresent()) {
                timeSlotTables = timeSlotTables.stream()
                        .filter(slot -> slot.getTimeSlot().getDate().equals(filters.date().get()))
                        .toList();
            }

            if (filters.time().isPresent()) {
                timeSlotTables = timeSlotTables.stream()
                        .filter(slot -> !slot.getTimeSlot().getTime().isBefore(filters.time().get()))
                        .toList();
            }

            if (filters.location().isPresent()) {
                timeSlotTables = timeSlotTables.stream()
                        .filter(slot -> slot.getTable().getLocation().equals(filters.location().get()))
                        .toList();
            }

            if (filters.typeOfCuisine().isPresent()) {
                String typeOfCuisineFilter = filters.typeOfCuisine().get();
                timeSlotTables = timeSlotTables.stream()
                        .filter(slot -> {
                            String cuisines = slot.getTable().getRestaurant().getCuisineType();
                            if (cuisines == null || cuisines.isEmpty()) return false;
                            List<String> cuisineList = List.of(cuisines.split(","));
                            return cuisineList.contains(typeOfCuisineFilter);
                        })
                        .toList();
            }

            if (filters.zone().isPresent()) {
                timeSlotTables = timeSlotTables.stream()
                        .filter(slot -> slot.getTable().getZone().equals(filters.zone().get()))
                        .toList();
            }

            if (filters.numberOfSeats().isPresent()) {
                timeSlotTables = timeSlotTables.stream()
                        .filter(slot -> slot.getTable().getSeats() >= filters.numberOfSeats().get())
                        .toList();
            }

            if (filters.restaurantId().isPresent()) {
                timeSlotTables = timeSlotTables.stream()
                        .filter(slot -> slot.getTable().getRestaurant().getId().equals(filters.restaurantId().get()))
                        .toList();
            }


        }

        return timeSlotTables.stream()
                .map(slot -> new TimeSlotTableDTO(
                        slot.getId(),
                        slot.getAvailable(),
                        new TablesDTOt(
                                slot.getTable().getId(),
                                slot.getTable().getSeats(),
                                slot.getTable().getZone(),
                                slot.getTable().getLocation()
                        ),
                        new TimeSlotDTOt(
                                slot.getTimeSlot().getId(),
                                slot.getTimeSlot().getDate(),
                                slot.getTimeSlot().getTime()
                        ),
                        slot.getTimeSlot().getRestaurant().getId(),
                        slot.getTimeSlot().getRestaurant().getName()
                ))
                .toList();
    }

    public void update(TimeSlotTables timeSlotTables) {
        timeSlotTableRepository.save(timeSlotTables);
    }

}
