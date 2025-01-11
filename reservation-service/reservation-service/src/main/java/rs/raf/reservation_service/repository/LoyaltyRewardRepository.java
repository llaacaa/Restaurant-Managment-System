package rs.raf.reservation_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.raf.reservation_service.models.LoyaltyReward;

import java.util.List;

@Repository
public interface LoyaltyRewardRepository extends JpaRepository<LoyaltyReward, Long> {
    List<LoyaltyReward> findByRestaurantId(Long restaurantId);

    LoyaltyReward findByCondition(Integer condition);
}

