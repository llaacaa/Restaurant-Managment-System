package rs.raf.reservation_service.models.DTOs;

public record TimeSlotTableDTO(
        Long id,
        boolean isAvailable,
        TablesDTOt table,
        TimeSlotDTOt timeSlot
) {}
