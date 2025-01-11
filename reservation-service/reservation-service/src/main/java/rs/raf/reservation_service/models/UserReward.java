package rs.raf.reservation_service.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class UserReward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    private Integer nextRewardAt;
    private Boolean rewardUsed;

    public UserReward() {
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

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public Integer getNextRewardAt() {
        return nextRewardAt;
    }

    public void setNextRewardAt(Integer nextRewardAt) {
        this.nextRewardAt = nextRewardAt;
    }

    public Boolean getRewardUsed() {
        return rewardUsed;
    }

    public void setRewardUsed(Boolean rewardUsed) {
        this.rewardUsed = rewardUsed;
    }
}

