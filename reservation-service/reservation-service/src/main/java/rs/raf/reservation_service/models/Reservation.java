package rs.raf.reservation_service.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private LocalDate reservationDate;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    @ManyToOne
    @JoinColumn(name = "user_reward")
    private UserReward userReward;

    @OneToOne
    @JoinColumn(name = "time_slots_and_tables")
    private TimeSlotTables timeSlotTables;

    public Reservation() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDate reservationDate) {
        this.reservationDate = reservationDate;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public TimeSlotTables getTimeSlotTables() {
        return timeSlotTables;
    }

    public UserReward getUserReward() {
        return userReward;
    }

    public void setUserReward(UserReward userReward) {
        this.userReward = userReward;
    }

    public void setTimeSlotTables(TimeSlotTables timeSlotTables) {
        this.timeSlotTables = timeSlotTables;
    }
}
