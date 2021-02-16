package app.service;

import app.dao.MealRepository;
import app.dao.RestaurantRepository;
import app.entity.Meal;
import app.entity.Restaurant;
import app.service.exceptions.EntityNotFoundException;
import app.service.validation.ValidationUtil;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static app.service.utils.RestaurantPaginationSettings.*;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class RestaurantService {

    public enum ListView {
        SHORT,
        DETAILED
    }

    private final Logger logger = LoggerFactory.getLogger(app.service.RestaurantService.class);

    private final RestaurantRepository restaurantRepository;
    private final MealRepository mealRepository;

    public RestaurantService(RestaurantRepository restaurantRepository, MealRepository mealRepository) {
        this.restaurantRepository = restaurantRepository;
        this.mealRepository = mealRepository;
    }

    // Retrieve Methods ----------------------------------------------------------

    public List<Restaurant> getAllRestaurants(ListView view) {
        view = (view == null ? ListView.SHORT : view);
        Pageable pageable = PageRequest.of(
                DEFAULT_CURRENT_PAGE,
                DEFAULT_PAGE_SIZE,
                Sort.by(DEFAULT_SORT_DIRECTION, DEFAULT_SORTED_BY.getFieldName()));
        return getAllRestaurants(view, pageable);
    }

    // Generates 1 SELECT query (due to lazy loading)
    // Generates 1 more SELECT query to fetch all child collections (due to FetchMode.SUBSELECT)
    // Usage of FetchMode.JOIN (instead of FetchMode.SUBSELECT) doesn't allow paginating at database level
    @Cacheable(cacheNames = "restaurantsCache", sync = true)
    // This is inefficient way of caching (entire collection instead of individual entities of the collection)
    // https://stackoverflow.com/questions/44529029/spring-cache-with-collection-of-items-entities
    public List<Restaurant> getAllRestaurants(ListView view, Pageable pageable) {
        if (pageable == null) {
            pageable = PageRequest.of(
                    DEFAULT_CURRENT_PAGE,
                    DEFAULT_PAGE_SIZE,
                    Sort.by(DEFAULT_SORT_DIRECTION, DEFAULT_SORTED_BY.getFieldName()));
        }
        view = (view == null ? ListView.SHORT : view);
        List<Restaurant> restaurants = restaurantRepository.findAll(pageable).getContent();
        if (view.name().equalsIgnoreCase("DETAILED")) {
            restaurants.forEach(restaurant -> Hibernate.initialize(restaurant.getMeals()));
        }
        logger.info("Restaurant Service layer: Returning all restaurants.");
        return restaurants;
    }

    // Generates 1 SELECT query despite global fetch strategy FetchMode.SUBSELECT (see RestaurantRepository.class)
    // Without FetchMode.SUBSELECT findById() method of Spring Data can be directly used because FetchMode.JOIN is default strategy for to-Many relations
    public Restaurant getRestaurantById(Integer id) {
        ValidationUtil.checkNotNullEntityId(id);
        Restaurant restaurant = restaurantRepository.getRestaurantById(id);
        if (restaurant == null) {
            throw new EntityNotFoundException("Restaurant with id=" + id + " not found.");
        }
        logger.info("Restaurant Service layer: Returning restaurant with id = {}.", id);
        return restaurant;
    }

    // Generates 1 SELECT query (due to FetchMode.JOIN for restaurant property of Meal entity)
    // Without FetchMode.JOIN, 2 queries are generated because FetchMode.SELECT is default strategy for to-One relations
    public Meal getMealById(Integer id) {
        ValidationUtil.checkNotNullEntityId(id);
        logger.info("Restaurant Service layer: Returning meal with id = {}", id);
        return mealRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Meal with id=" + id + " not found."));
    }

    // Delete Methods ------------------------------------------------------------

    @CacheEvict(cacheNames = "restaurantsCache", allEntries = true)
    public void deleteAllRestaurants() {
        restaurantRepository.deleteAll();
        logger.info("Restaurant Service layer: All restaurants have been removed.");
    }

    @CacheEvict(cacheNames = "restaurantsCache", allEntries = true)
    public void deleteRestaurantById(Integer id) {
        ValidationUtil.checkNotNullEntityId(id);
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant with id=" + id + " not found."));
        restaurantRepository.deleteById(id);
        logger.info("Restaurant Service layer: Restaurant with id = {} has been removed.", id);
    }

    @CacheEvict(cacheNames = "restaurantsCache", allEntries = true)
    public void deleteMealById(Integer id) {
        ValidationUtil.checkNotNullEntityId(id);
        Meal meal = mealRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Meal with id=" + id + " not found."));
        Restaurant restaurant = meal.getRestaurant();
        restaurant.removeMeal(meal);
        logger.info("Restaurant Service layer: Meal with id = {} has been removed.", id);
    }

    @CacheEvict(cacheNames = "restaurantsCache", allEntries = true)
    public void deleteAllMeals() {
        List<Restaurant> restaurants = restaurantRepository.findAll();
        for (Restaurant restaurant : restaurants) {
            restaurant.removeMeals();
        }
        logger.info("Restaurant Service layer: All meals have been removed.");
    }

    @CacheEvict(cacheNames = "restaurantsCache", allEntries = true)
    public void deleteAllMealsForRestaurantWithId(Integer id) {
        ValidationUtil.checkNotNullEntityId(id);
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant with id=" + id + " not found."));
        restaurant.removeMeals();
        logger.info("Restaurant Service layer: All meals for restaurant with id = {} have been removed.", id);
    }

    // Create Methods -------------------------------------------------------------

    @CacheEvict(cacheNames = "restaurantsCache", allEntries = true)
    public Integer createRestaurant(Restaurant restaurant) {
        ValidationUtil.checkNotNullEntityInstance(restaurant);
        ValidationUtil.checkNullEntityId(restaurant.getId());
        // Creation of restaurant without name is not allowed
        ValidationUtil.checkNotNullRestaurantEntityProperties(restaurant);
        // Creation of restaurant with empty list of meals or without meals property at all is allowed
        if (restaurant.getMeals() == null || restaurant.getMeals().isEmpty()) {
            restaurant.setMeals(new ArrayList<>());
        }
        else {
            restaurant.getMeals().forEach(meal -> {
                ValidationUtil.checkNotNullEntityInstance(meal);
                ValidationUtil.checkNullEntityId(meal.getId());
                ValidationUtil.checkNotNullMealEntityProperties(meal);
            });
        }
        // Creation of restaurant with non-zero votes is not allowed
        restaurant.setVotes(0);

        restaurantRepository.save(restaurant);
        restaurantRepository.flush();
        Integer id = restaurant.getId();
        logger.info("Restaurant Service layer: Creating new restaurant with id = {}.", id);
        return id;
    }

    @CacheEvict(cacheNames = "restaurantsCache", allEntries = true)
    public Integer createMealForRestaurantWithId(Integer id, Meal meal) {
        ValidationUtil.checkNotNullEntityId(id);
        ValidationUtil.checkNotNullEntityInstance(meal);
        ValidationUtil.checkNullEntityId(meal.getId());
        ValidationUtil.checkNotNullMealEntityProperties(meal);
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Restaurant with id=" + id + " not found."));
        restaurant.addMeal(meal);
        restaurantRepository.flush();
        logger.info("Restaurant Service layer: Creating new meal for restaurant with id = {}.", id);
        return meal.getId();
    }

    // Update Methods -------------------------------------------------------

    @CacheEvict(cacheNames = "restaurantsCache", allEntries = true)
    public void updateRestaurantById(Integer id, Restaurant restaurant) {
        ValidationUtil.checkNotNullEntityId(id);
        ValidationUtil.checkNotNullEntityInstance(restaurant);
        ValidationUtil.checkNotNullRestaurantEntityProperties(restaurant);
        Restaurant r = restaurantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant with id=" + id + " not found."));
        r.setName(restaurant.getName());
        // Manual updating of votes is not allowed
        if (restaurant.getMeals() != null) {
            restaurant.getMeals().forEach(meal -> {
                ValidationUtil.checkNotNullEntityInstance(meal);
                ValidationUtil.checkNullEntityId(meal.getId());
                ValidationUtil.checkNotNullMealEntityProperties(meal);
            });
            r.removeMeals();
            restaurantRepository.flush();
            r.addMeals(restaurant.getMeals());
        }
        logger.info("Restaurant Service layer: Updating restaurant with id = {}.", id);
        restaurantRepository.flush();
    }

    @CacheEvict(cacheNames = "restaurantsCache", allEntries = true)
    public void updateMealById(Integer id, Meal meal) {
        ValidationUtil.checkNotNullEntityId(id);
        ValidationUtil.checkNotNullEntityInstance(meal);
        ValidationUtil.checkNotNullMealEntityProperties(meal);
        Meal m = mealRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Meal with id=" + id + " not found."));
        m.setName(meal.getName());
        m.setCategory(meal.getCategory());
        m.setPrice(meal.getPrice());
        logger.info("Restaurant Service layer: Updating meal with id = {}.", id);
        mealRepository.flush();
    }

}
