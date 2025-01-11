package rs.raf.reservation_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf.reservation_service.middleware.AuthorizationService;
import rs.raf.reservation_service.middleware.ManagerAuthDetails;
import rs.raf.reservation_service.middleware.UnauthorizedException;
import rs.raf.reservation_service.models.Tables;
import rs.raf.reservation_service.models.TimeSlotTables;
import rs.raf.reservation_service.models.requests.ManageTablesRequest;
import rs.raf.reservation_service.service.TablesService;
import rs.raf.reservation_service.service.TimeSlotTablesService;

import java.util.Optional;

@RestController
@RequestMapping("/table")
public class TablesController {
    private final TablesService tablesService;
    private final AuthorizationService authorizationService;
    private final TimeSlotTablesService timeSlotTablesService;

    public TablesController(TablesService tablesService, AuthorizationService authorizationService, TimeSlotTablesService timeSlotTablesService) {
        this.tablesService = tablesService;
        this.authorizationService = authorizationService;
        this.timeSlotTablesService = timeSlotTablesService;
    }

    // TODO TEST THIS

    @PostMapping("/{id}/update")
    public ResponseEntity<?> updateTable(@RequestBody ManageTablesRequest tableData, @PathVariable Long id, @RequestHeader("Authorization") String authorizationHeader) {
        try {
            ManagerAuthDetails authDetails = authorizationService.validateManagerAuthorization(authorizationHeader, id);

            Optional<Tables> tableToUpdate = tablesService.getTable(id);
            if (!tableToUpdate.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            Tables table = tableToUpdate.get();
            if (tableData.availability().isPresent()) {
                TimeSlotTables timeSlotTables = tableData.timeSlotId().isPresent() ? timeSlotTablesService.findByTimeSlotAndTable(tableData.timeSlotId().get(), table.getId()) : null;
                if (timeSlotTables != null) {
                    timeSlotTables.setAvailable(tableData.availability().get());
                    timeSlotTablesService.changeAvailability(timeSlotTables);
                }
            }

            table.setLocation(tableData.location());
            table.setZone(tableData.zone());
            table.setSeats(tableData.seats());
            tablesService.updateTable(table);

            return new ResponseEntity<>("Table successfully updated", HttpStatus.OK);
        } catch (
                UnauthorizedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("Internal error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
