package app.controller;

import app.controller.views.RestaurantView;
import app.entity.Meal;
import app.entity.Restaurant;
import app.service.RestaurantService;
import app.service.utils.RestaurantSorter;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

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
    @Cacheable(cacheNames = "restaurantsCache", sync = true)
    // Ideally @Cacheable have to be declared at service layer.
    // But! In this particular case declaring this annotation at getAllRestaurants method of service layer
    // leads to LazyInitializationException when controller's getAllRestaurants method is firstly invoked
    // with view=brief request param and after that with view=detailed (because service layer has no idea
    // about 'view' param so in both requests the cache key is the same -> pageable object.
    // ---
    // This is the very inefficient way of caching (collection instead of individual entities)
    // https://stackoverflow.com/questions/44529029/spring-cache-with-collection-of-items-entities
    public ResponseEntity<MappingJacksonValue> getAllRestaurants(
            @RequestParam(required = false, defaultValue = "brief") String view,
            @RequestParam(required = false, defaultValue = "0") Integer currentPage,
            @RequestParam(required = false, defaultValue = "100") Integer pageSize,
            @RequestParam(required = false, defaultValue = "votes") RestaurantSorter sort,
            @RequestParam(required = false, defaultValue = "desc") Sort.Direction sdir) {

        Pageable pageable = PageRequest.of(currentPage, pageSize, Sort.by(sdir, sort.getFieldName()));
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

    @DeleteMapping("/restaurants")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(cacheNames = "restaurantsCache", allEntries = true)
    public void deleteAllRestaurants() {
        restaurantService.deleteAllRestaurants();
        logger.info("Controller layer: deleting all restaurants.");
    }

    @DeleteMapping("/restaurants/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(cacheNames = "restaurantsCache", allEntries = true)
    public void deleteRestaurantById(@PathVariable Integer id) {
        restaurantService.deleteRestaurantById(id);
        logger.info("Controller layer: Deleting restaurant with id = {}.", id);
    }

    @DeleteMapping("/restaurants/{id}/meals")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(cacheNames = "restaurantsCache", allEntries = true)
    public void deleteAllMealsForRestaurantWithId(@PathVariable Integer id) {
        restaurantService.deleteAllMealsForRestaurantWithId(id);
        logger.info("Controller layer: Deleting all meals for restaurant with id = {}.", id);
    }

    @DeleteMapping("/restaurants/meals")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(cacheNames = "restaurantsCache", allEntries = true)
    public void deleteAllMeals() {
        restaurantService.deleteAllMeals();
        logger.info("Controller layer: Deleting all meals for all restaurants.");
    }

    @DeleteMapping("/restaurants/meals/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @CacheEvict(cacheNames = "restaurantsCache", allEntries = true)
    public void deleteMealById(@PathVariable Integer id) {
        restaurantService.deleteMealById(id);
        logger.info("Controller layer: Deleting meal with id = {}.", id);
    }

}

