package rs.raf.reservation_service.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import rs.raf.reservation_service.models.DTOs.TimeSlotDTO;
import rs.raf.reservation_service.models.Tables;
import rs.raf.reservation_service.models.TimeSlot;
import rs.raf.reservation_service.models.TimeSlotTables;
import rs.raf.reservation_service.repository.TablesRepository;
import rs.raf.reservation_service.repository.TimeSlotRepository;
import rs.raf.reservation_service.repository.TimeSlotTableRepository;
import rs.raf.reservation_service.service.mappers.ClassToDTO.TimeSlotDtoMapper;
import rs.raf.reservation_service.service.mappers.RequestToClass.RequestTimeSlotMapper;

import java.util.List;

@Service
public class TimeSlotService {
    private final TimeSlotRepository timeSlotRepository;
    private final RequestTimeSlotMapper requestTimeSlotMapper;
    private final TimeSlotDtoMapper timeSlotDtoMapper;

    private final TablesRepository tablesRepository;
    private final TimeSlotTableRepository timeSlotTableRepository;

    public TimeSlotService(TimeSlotRepository timeSlotRepository, RequestTimeSlotMapper requestTimeSlotMapper,
                           TimeSlotDtoMapper timeSlotDtoMapper, TablesRepository tablesRepository, TimeSlotTableRepository timeSlotTableRepository) {
        this.timeSlotRepository = timeSlotRepository;
        this.requestTimeSlotMapper = requestTimeSlotMapper;
        this.timeSlotDtoMapper = timeSlotDtoMapper;
        this.tablesRepository = tablesRepository;
        this.timeSlotTableRepository = timeSlotTableRepository;
    }

    public TimeSlot createTimeSlot(TimeSlotDTO timeSlotDTO) {
        return timeSlotRepository.save(requestTimeSlotMapper.apply(timeSlotDTO));
    }

    @Transactional
    public TimeSlot addTimeSlot(TimeSlot timeSlot) {
        TimeSlot savedTimeSlot = timeSlotRepository.save(timeSlot);

        List<Tables> tables = tablesRepository.findAll();

        List<TimeSlotTables> timeSlotTables = tables.stream()
                .map(table -> {
                    TimeSlotTables timeSlotTable = new TimeSlotTables();
                    timeSlotTable.setTimeSlot(savedTimeSlot);
                    timeSlotTable.setTable(table);
                    return timeSlotTable;
                })
                .toList();

        timeSlotTableRepository.saveAll(timeSlotTables);

        return savedTimeSlot;
    }
}
