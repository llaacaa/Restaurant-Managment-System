package rs.raf.users_service.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import java.time.LocalDate;

@Entity
public class Clients extends Users {

    @Column(name = "number_of_reservations", nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer numberOfReservations = 0;

    public Clients() {
    }

    public Clients(String username, String password, String email,
                   LocalDate dateOfBirth, String name,
                   String surname) {
        super(username, password, email, dateOfBirth, name, surname, ROLES.CLIENT);
    }

    public void setNumberOfReservations(Integer numberOfReservations) {
        this.numberOfReservations = numberOfReservations;
    }

    public Integer getNumberOfReservations() {
        return numberOfReservations;
    }
}
