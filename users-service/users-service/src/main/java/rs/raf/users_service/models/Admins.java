package rs.raf.users_service.models;

import jakarta.persistence.Entity;

import java.time.LocalDate;

@Entity
public class Admins extends Users {
    public Admins() {
    }

    public Admins(String username, String password, String email, LocalDate dateOfBirth, String name, String surname) {
        super(username, password, email, dateOfBirth, name, surname, ROLES.ADMIN);
        this.setActive(true);
    }
}
