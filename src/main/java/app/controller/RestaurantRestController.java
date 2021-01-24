package app.controller;

import app.entity.Meal;
import app.entity.Restaurant;
import app.controller.views.RestaurantView;
import app.service.RestaurantService;
import app.service.utils.RestaurantSorter;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@Transactional
public class RestaurantRestController {

    private final Logger logger = LoggerFactory.getLogger(app.controller.RestaurantRestController.class);

    private final RestaurantService restaurantService;

    public RestaurantRestController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping("/restaurants")
    public ResponseEntity<MappingJacksonValue> getAllRestaurants(
            @RequestParam(required = false, defaultValue = "brief") String view,
            @RequestParam(required = false, defaultValue = "0") Integer currentPage,
            @RequestParam(required = false, defaultValue = "100") Integer pageSize,
            @RequestParam(required = false, defaultValue = "votes") RestaurantSorter sorter,
            @RequestParam(required = false, defaultValue = "desc") Sort.Direction sortDirection) {

        Pageable pageable = PageRequest.of(currentPage, pageSize, Sort.by(sortDirection, sorter.getFieldName()));
        List<Restaurant> restaurants = restaurantService.getAllRestaurants(pageable);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=UTF-8");
        headers.add("Cache-Control", "no-store");

        // https://stackoverflow.com/questions/23665107/select-jsonview-in-the-spring-mvc-controller
        MappingJacksonValue value = new MappingJacksonValue(restaurants);
        if("detailed".equals(view)) {
            restaurants.forEach(restaurant -> Hibernate.initialize(restaurant.getMeals()));
            value.setSerializationView(RestaurantView.Detailed.class);
        }
        else {
            value.setSerializationView(RestaurantView.Brief.class);
        }

        logger.info("Controller layer: Returning all restaurants.");
        return new ResponseEntity<>(value, headers, HttpStatus.OK);
    }

    @GetMapping("/restaurants/{id}")
    public ResponseEntity<Restaurant> getRestaurantById(@PathVariable Integer id) {

        Restaurant restaurant = restaurantService.getRestaurantById(id);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=UTF-8");
        headers.add("Cache-Control", "no-store");

        logger.info("Controller layer: Returning restaurant with id = {}.", id);
        return new ResponseEntity<>(restaurant, headers, HttpStatus.OK);
    }

    @GetMapping("/restaurants/meals/{id}")
    public ResponseEntity<Meal> getMealById(@PathVariable Integer id) {

        Meal meal = restaurantService.getMealById(id);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=UTF-8");
        headers.add("Cache-Control", "no-store");

        logger.info("Controller layer: Returning meal with id = {}.", id);
        return new ResponseEntity<>(meal, headers, HttpStatus.OK);
    }

}

