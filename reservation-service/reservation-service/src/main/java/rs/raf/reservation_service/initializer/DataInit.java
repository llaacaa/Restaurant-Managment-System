package rs.raf.reservation_service.initializer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import rs.raf.reservation_service.models.*;
import rs.raf.reservation_service.repository.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
public class DataInit implements CommandLineRunner {

    private final RestaurantRepository restaurantRepository;
    private final TablesRepository tableRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final TimeSlotTableRepository timeSlotTableRepository;

    public DataInit(RestaurantRepository restaurantRepository,
                    TablesRepository tableRepository,
                    TimeSlotRepository timeSlotRepository,
                    TimeSlotTableRepository timeSlotTableRepository) {
        this.restaurantRepository = restaurantRepository;
        this.tableRepository = tableRepository;
        this.timeSlotRepository = timeSlotRepository;
        this.timeSlotTableRepository = timeSlotTableRepository;
    }

    @Override
    public void run(String... args) {
        // Create a restaurant
        Restaurant restaurant = new Restaurant();
        restaurant.setName("Lacin restoran");
        restaurant.setAddress("456 Elm St, Boston");
        restaurant.setDescription("Fine dining experience");
        restaurant.setTableCount(3);
        restaurant.setWorkingHours("12:00-23:00");
        restaurant.setCuisineType("FRENCH");
        restaurant = restaurantRepository.save(restaurant);

        Restaurant restaurant1 = new Restaurant();
        restaurant1.setName("Neki znji");
        restaurant1.setAddress("456 Elm St, Boston");
        restaurant1.setDescription("Fine dining experience");
        restaurant1.setTableCount(3);
        restaurant1.setWorkingHours("12:00-23:00");
        restaurant1.setCuisineType("FRENCH");
        restaurant1 = restaurantRepository.save(restaurant1);

        // Create tables
        Tables table1 = new Tables();
        table1.setSeats(4);
        table1.setZone(Zone.SMOKING);
        table1.setLocation(Location.OUTDOOR);
        table1.setRestaurant(restaurant);

        Tables table2 = new Tables();
        table2.setSeats(2);
        table2.setZone(Zone.NON_SMOKING);
        table2.setLocation(Location.INDOOR);
        table2.setRestaurant(restaurant);

        table1 = tableRepository.save(table1);
        table2 = tableRepository.save(table2);

        // Create time slots
        TimeSlot timeSlot1 = new TimeSlot();
        timeSlot1.setDate(LocalDate.of(2025, 1, 15));
        timeSlot1.setTime(LocalTime.of(18, 0));
        timeSlot1.setRestaurant(restaurant);

        TimeSlot timeSlot2 = new TimeSlot();
        timeSlot2.setDate(LocalDate.of(2025, 1, 15));
        timeSlot2.setTime(LocalTime.of(20, 0));
        timeSlot2.setRestaurant(restaurant);

        timeSlot1 = timeSlotRepository.save(timeSlot1);
        timeSlot2 = timeSlotRepository.save(timeSlot2);

        // Create time slot-table mappings with availability
        TimeSlotTables slotTable1 = new TimeSlotTables();
        slotTable1.setTimeSlot(timeSlot1);
        slotTable1.setTable(table1);

        TimeSlotTables slotTable2 = new TimeSlotTables();
        slotTable2.setTimeSlot(timeSlot1);
        slotTable2.setTable(table2);

        TimeSlotTables slotTable3 = new TimeSlotTables();
        slotTable3.setTimeSlot(timeSlot2);
        slotTable3.setTable(table1);

        timeSlotTableRepository.saveAll(List.of(slotTable1, slotTable2, slotTable3));

        System.out.println("Data initialization completed.");
    }
}


