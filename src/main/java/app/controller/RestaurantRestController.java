package app.controller;

import app.entity.Restaurant;
import app.service.RestaurantService;
import app.service.utils.RestaurantSorter;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/restaurants")
@Transactional
public class RestaurantRestController {

    private final Logger logger = LoggerFactory.getLogger(app.controller.RestaurantRestController.class);

    private final RestaurantService restaurantService;

    public RestaurantRestController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping
    public ResponseEntity<List<Restaurant>> getAll(
            @RequestParam(name = "currentPage", required = false) Integer currentPage,
            @RequestParam(name = "pageSize", required = false) Integer pageSize,
            @RequestParam(name = "sortBy", required = false) RestaurantSorter sorter,
            @RequestParam(name = "sortDirection", required = false) Sort.Direction sortDirection) {

        List<Restaurant> restaurants = restaurantService.getAllRestaurants(null);
        restaurants.forEach(restaurant -> Hibernate.initialize(restaurant.getMeals()));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=UTF-8");
        headers.add("Cache-Control", "no-store");

        logger.info("Controller layer: Returning all restaurants");
        return new ResponseEntity<>(restaurants, headers, HttpStatus.OK);
    }

}

