package app.service;

import app.dao.MealRepository;
import app.dao.RestaurantRepository;
import app.entity.Meal;
import app.entity.Restaurant;
import app.exceptions.EntityNotFoundException;
import app.service.utils.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static app.service.utils.PaginationSettings.*;

@Service
@Transactional
public class RestaurantService {

    private final Logger logger = LoggerFactory.getLogger(app.service.RestaurantService.class);

    private final RestaurantRepository restaurantRepository;
    private final MealRepository mealRepository;

    public RestaurantService(RestaurantRepository restaurantRepository, MealRepository mealRepository) {
        this.restaurantRepository = restaurantRepository;
        this.mealRepository = mealRepository;
    }

    // Retrieve Methods ----------------------------------------------------------

    // generates 1 select query (due to lazy loading) if child collections are not requested
    // generates 1 more select query to fetch all child collections (due to FetchMode.SUBSELECT)
    // usage of JOIN FETCH (instead of default FetchMode.SUBSELECT) doesn't allow paginating at database level
    public List<Restaurant> getAllRestaurants(@Nullable Pageable pageable) {
        if (pageable == null) {
            pageable = PageRequest.of(
                    DEFAULT_CURRENT_PAGE,
                    DEFAULT_PAGE_SIZE,
                    Sort.by(DEFAULT_SORT_DIRECTION, DEFAULT_SORTED_BY.getFieldName()));
        }
        logger.info("Restaurant Service layer: Returning all restaurants.");
        return restaurantRepository.findAll(pageable).getContent();
    }

    // generates maximum 1 select query despite global fetch strategy FetchMode.SUBSELECT (see RestaurantRepository.class)
    // without FetchMode.SUBSELECT SpringData findById() can be directly used because FetchMode.JOIN is default for toMany relations
    public Restaurant getRestaurantById(Integer id) {
        ValidationUtil.checkNotNullId(id);
        Restaurant restaurant = restaurantRepository.getRestaurantById(id);
        if (restaurant == null) {
            throw new EntityNotFoundException("Restaurant with id=" + id + " not found.");
        }
        logger.info("Restaurant Service layer: Returning restaurant with id = {}.", id);
        return restaurant;
    }

    // generates maximum 1 select query (due to FetchMode.JOIN for Meal entity)
    // without FetchMode.JOIN, 2 queries are generated because FetchMode.SELECT is default for toOne relations
    public Meal getMealById(Integer id) {
        ValidationUtil.checkNotNullId(id);
        logger.info("Restaurant Service layer: Returning meal with id = {}", id);
        return mealRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Meal with id=" + id + " not found."));
    }

    // Delete Methods ------------------------------------------------------------

    public void deleteAllRestaurants() {
        restaurantRepository.deleteAll();
        restaurantRepository.flush();
        logger.info("Restaurant Service layer: All restaurants have been removed.");
    }

    public void deleteRestaurantById(Integer id) {
        ValidationUtil.checkNotNullId(id);
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant with id=" + id + " not found."));
        restaurantRepository.deleteById(id);
        restaurantRepository.flush();
        logger.info("Restaurant Service layer: Restaurant with id = {} has been removed.", id);
    }

    public void deleteMealById(Integer id) {
        ValidationUtil.checkNotNullId(id);
        Meal meal = mealRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Meal with id=" + id + " not found."));
        Restaurant restaurant = meal.getRestaurant();
        restaurant.removeMeal(meal);
        restaurantRepository.flush();
        logger.info("Restaurant Service layer: Meal with id = {} has been removed.", id);
    }

    public void deleteAllMeals() {
        List<Restaurant> restaurants = restaurantRepository.findAll();
        for (Restaurant restaurant : restaurants) {
            restaurant.removeMeals();
        }
        restaurantRepository.flush();
        logger.info("Restaurant Service layer: All meals have been removed.");
    }

    public void deleteAllMealsForRestaurantWithId(Integer id) {
        ValidationUtil.checkNotNullId(id);
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant with id=" + id + " not found."));
        restaurant.removeMeals();
        restaurantRepository.flush();
        logger.info("Restaurant Service layer: All meals for restaurant with id = {} have been removed.", id);
    }

    // Create Methods -------------------------------------------------------------

    public Integer createRestaurant(Restaurant restaurant) {
        ValidationUtil.checkNotNullInstance(restaurant);
        Integer id = restaurant.getId();
        ValidationUtil.checkNullId(id);
        ValidationUtil.checkNotNullProperties(restaurant);
        restaurantRepository.save(restaurant);
        restaurantRepository.flush();
        logger.info("Restaurant Service layer: Creating new restaurant.");
        return restaurant.getId();
    }

    public Integer createMealForRestaurantWithId(Integer id, Meal meal) {
        ValidationUtil.checkNotNullId(id);
        ValidationUtil.checkNotNullInstance(meal);
        ValidationUtil.checkNullId(meal.getId());
        ValidationUtil.checkNotNullProperties(meal);
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Restaurant with id=" + id + " not found."));
        restaurant.addMeal(meal);
        restaurantRepository.flush();
        logger.info("Restaurant Service layer: Creating new meal for restaurant with id = {}.", id);
        return meal.getId();
    }

    // Update Methods -------------------------------------------------------

    public void updateRestaurantById(Integer id, Restaurant restaurant) {
        ValidationUtil.checkNotNullId(id);
        ValidationUtil.checkNotNullInstance(restaurant);
        ValidationUtil.checkNotNullProperties(restaurant);
        Restaurant r = restaurantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant with id=" + id + " not found."));
        r.setName(restaurant.getName());
        r.setVotes(restaurant.getVotes());
        r.removeMeals();
        r.addMeals(restaurant.getMeals());
        logger.info("Restaurant Service layer: Updating restaurant with id = {}.", id);
        restaurantRepository.flush();
    }

    public void updateMealById(Integer id, Meal meal) {
        ValidationUtil.checkNotNullId(id);
        ValidationUtil.checkNotNullInstance(meal);
        ValidationUtil.checkNotNullProperties(meal);
        Meal m = mealRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Meal with id=" + id + " not found."));
        m.setName(meal.getName());
        m.setCategory(meal.getCategory());
        m.setPrice(meal.getPrice());
        logger.info("Restaurant Service layer: Updating meal with id = {}.", id);
        mealRepository.flush();
    }

}
