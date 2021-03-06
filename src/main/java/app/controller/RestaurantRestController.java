package app.controller;

import app.controller.exceptions.EntityValidationException;
import app.controller.views.RestaurantView;
import app.entity.Meal;
import app.entity.Restaurant;
import app.service.RestaurantService;
import app.service.helpers.RestaurantSorter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

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
            @RequestParam(required = false, defaultValue = "votes") RestaurantSorter sort,
            @RequestParam(required = false, defaultValue = "desc") Sort.Direction sdir) {

        Pageable pageable = PageRequest.of(currentPage, pageSize, Sort.by(sdir, sort.getFieldName()));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=UTF-8");
        headers.add("Cache-Control", "no-store");

        List<Restaurant> restaurants = null;
        MappingJacksonValue value = null;

        if("detailed".equalsIgnoreCase(view)) {
            restaurants = restaurantService.getAllRestaurants(RestaurantService.ListView.DETAILED, pageable);
            value = new MappingJacksonValue(restaurants);
            value.setSerializationView(RestaurantView.Detailed.class);
        }
        else {
            restaurants = restaurantService.getAllRestaurants(RestaurantService.ListView.SHORT, pageable);
            value = new MappingJacksonValue(restaurants);
            value.setSerializationView(RestaurantView.Brief.class);
        }

        logger.info("Controller layer: All restaurants have been returned in response.");
        return new ResponseEntity<>(value, headers, HttpStatus.OK);
    }

    @GetMapping("/restaurants/{id}")
    public ResponseEntity<Restaurant> getRestaurantById(@PathVariable Integer id) {
        Restaurant restaurant = restaurantService.getRestaurantById(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=UTF-8");
        headers.add("Cache-Control", "no-store");
        logger.info("Restaurant Controller layer: Restaurant with id = {} has been returned in response.", id);
        return new ResponseEntity<>(restaurant, headers, HttpStatus.OK);
    }

    @GetMapping("/restaurants/meals/{id}")
    public ResponseEntity<Meal> getMealById(@PathVariable Integer id) {
        Meal meal = restaurantService.getMealById(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json; charset=UTF-8");
        headers.add("Cache-Control", "no-store");
        logger.info("Restaurant Controller layer: Meal with id = {} has been returned in response.", id);
        return new ResponseEntity<>(meal, headers, HttpStatus.OK);
    }

    @DeleteMapping("/restaurants")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllRestaurants() {
        restaurantService.deleteAllRestaurants();
        logger.info("Restaurant Controller layer: All restaurants have been removed.");
    }

    @DeleteMapping("/restaurants/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRestaurantById(@PathVariable Integer id) {
        restaurantService.deleteRestaurantById(id);
        logger.info("Restaurant Controller layer: Restaurant with id = {} has been removed.", id);
    }

    @DeleteMapping("/restaurants/{id}/meals")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllMealsForRestaurantWithId(@PathVariable Integer id) {
        restaurantService.deleteAllMealsForRestaurantWithId(id);
        logger.info("Restaurant Controller layer: All meals for restaurant with id = {} have been removed.", id);
    }

    @DeleteMapping("/restaurants/meals")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllMeals() {
        restaurantService.deleteAllMeals();
        logger.info("Restaurant Controller layer: All meals for all restaurants have been removed.");
    }

    @DeleteMapping("/restaurants/meals/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMealById(@PathVariable Integer id) {
        restaurantService.deleteMealById(id);
        logger.info("Restaurant Controller layer: Meal with id = {} has been removed.", id);
    }

    @PostMapping("/restaurants")
    public ResponseEntity<String> createRestaurant(@RequestBody @Valid Restaurant restaurant, BindingResult bindingResult) {
        checkBindingResult(bindingResult);
        Integer id = restaurantService.createRestaurant(restaurant);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/restaurants/" + id);
        logger.info("Restaurant Controller layer: Restaurant with id = {} has been created.", id);
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PostMapping("/restaurants/{id}")
    public ResponseEntity<String> createMealForRestaurantWithId(@PathVariable Integer id, @RequestBody @Valid Meal meal, BindingResult bindingResult) {
        checkBindingResult(bindingResult);
        Integer meal_id = restaurantService.createMealForRestaurantWithId(id, meal);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/restaurants/meals/" + meal_id);
        logger.info("Restaurant Controller layer: Meal with id = {} has been created for restaurant with id = {}.", meal_id, id);
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PutMapping("/restaurants/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateRestaurantById(@PathVariable Integer id, @RequestBody @Valid Restaurant restaurant, BindingResult bindingResult) {
        checkBindingResult(bindingResult);
        restaurantService.updateRestaurantById(id, restaurant);
        logger.info("Restaurant Controller layer: Restaurant with id = {} has been updated.", id);
    }

    @PutMapping("/restaurants/meals/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateMealById(@PathVariable Integer id, @RequestBody @Valid Meal meal, BindingResult bindingResult) {
        checkBindingResult(bindingResult);
        restaurantService.updateMealById(id, meal);
        logger.info("Restaurant Controller layer: Meal with id = {} has been updated.", id);
    }

    private void checkBindingResult(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            logger.info("Restaurant Controller layer: Failed to validate the entity.");
            throw new EntityValidationException(errors);
        }
    }

}

