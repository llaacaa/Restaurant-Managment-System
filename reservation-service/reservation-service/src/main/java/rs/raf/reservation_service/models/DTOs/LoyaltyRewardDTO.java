package rs.raf.reservation_service.models.DTOs;

import rs.raf.reservation_service.models.RewardType;

public record LoyaltyRewardDTO(
        Long restaurantId,
        Integer condition,
        RewardType rewardType
) {
}
