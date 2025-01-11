package rs.raf.users_service.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import java.time.LocalDate;

@Entity
public class Managers extends Users {
    @Column(name = "restaurant_name", nullable = false, unique = true)
    private String restaurantName;

    @Column(name = "date_of_employment", nullable = false)
    private LocalDate dateOfEmployment;

    public Managers() {
    }

    public Managers(String username, String password, String email,
                    LocalDate dateOfBirth, String name, String surname,
                    String restaurantName, LocalDate dateOfEmployment) {
        super(username, password, email, dateOfBirth, name, surname, ROLES.MANAGER);
        this.restaurantName = restaurantName;
        this.dateOfEmployment = dateOfEmployment;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public LocalDate getDateOfEmployment() {
        return dateOfEmployment;
    }

    public void setDateOfEmployment(LocalDate dateOfEmployment) {
        this.dateOfEmployment = dateOfEmployment;
    }
}
