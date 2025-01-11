package rs.raf.reservation_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.raf.reservation_service.models.Restaurant;
import rs.raf.reservation_service.models.Tables;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    List<Restaurant> findByCuisineType(String cuisineType);

    Optional<Restaurant> findByName(String name);

    @Repository
    interface TableRepository extends JpaRepository<Tables, Long> {
    }
}


