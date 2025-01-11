package rs.raf.reservation_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.raf.reservation_service.models.DTOs.TimeSlotTableDTO;
import rs.raf.reservation_service.models.requests.TableFiltersRequest;
import rs.raf.reservation_service.service.TimeSlotTablesService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/free-time-slots")
public class TimeSlotTablesController {
    private final TimeSlotTablesService timeSlotTablesService;

    public TimeSlotTablesController(TimeSlotTablesService timeSlotTablesService) {
        this.timeSlotTablesService = timeSlotTablesService;
    }

    @GetMapping
    public List<TimeSlotTableDTO> getTimeSlots(@RequestBody Optional<TableFiltersRequest> filters) {
        return timeSlotTablesService.findAll(filters);
    }
}
