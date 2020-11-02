package app.service;

import app.dao.RestaurantRepository;
import app.entity.Meal;
import app.entity.Restaurant;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public Restaurant getById(Integer id) {
        Restaurant restaurant = restaurantRepository.getOne(id);
        List<Meal> meals = restaurant.getMeals();

        meals.forEach(System.out::println);

        return restaurant;
    }

}
