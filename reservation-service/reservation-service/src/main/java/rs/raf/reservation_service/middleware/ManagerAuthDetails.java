package rs.raf.reservation_service.middleware;

import rs.raf.reservation_service.models.DTOs.ManagerDTO;
import rs.raf.reservation_service.models.Restaurant;

public class ManagerAuthDetails {

    private final ManagerDTO manager;
    private final Restaurant restaurant;

    public ManagerAuthDetails(ManagerDTO manager, Restaurant restaurant) {
        this.manager = manager;
        this.restaurant = restaurant;
    }

    public ManagerDTO getManager() {
        return manager;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }
}
