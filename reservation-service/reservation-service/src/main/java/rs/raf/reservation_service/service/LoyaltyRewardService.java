package rs.raf.reservation_service.service;

import org.springframework.stereotype.Service;
import rs.raf.reservation_service.models.DTOs.LoyaltyRewardDTO;
import rs.raf.reservation_service.models.LoyaltyReward;
import rs.raf.reservation_service.models.Restaurant;
import rs.raf.reservation_service.repository.LoyaltyRewardRepository;

@Service
public class LoyaltyRewardService {
    private final LoyaltyRewardRepository loyaltyRewardRepository;

    public LoyaltyRewardService(LoyaltyRewardRepository loyaltyRewardRepository) {
        this.loyaltyRewardRepository = loyaltyRewardRepository;
    }

    public LoyaltyReward findLoyaltyRewardByCondition(Integer condition) {
        return loyaltyRewardRepository.findByCondition(condition);
    }

    public void createLoyaltyReward(LoyaltyRewardDTO loyaltyRewardDTO, Restaurant restaurant) {
        LoyaltyReward loyaltyReward = new LoyaltyReward();

        loyaltyReward.setCondition(loyaltyRewardDTO.condition());
        loyaltyReward.setRestaurant(restaurant);
        loyaltyReward.setType(loyaltyRewardDTO.rewardType());
        loyaltyRewardRepository.save(loyaltyReward);
    }
}
