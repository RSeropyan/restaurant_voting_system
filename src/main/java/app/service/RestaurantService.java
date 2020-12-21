package app.service;

import app.dao.MealRepository;
import app.dao.RestaurantRepository;
import app.entity.Meal;
import app.entity.Restaurant;
import app.exceptions.EntityNotFoundException;
import app.service.utils.ValidatorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static app.service.utils.PaginationUtil.*;

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

    public Restaurant getRestaurantById(Integer id) {
        ValidatorUtil.checkNotNullId(id);
        logger.info("Restaurant Service layer: Returning restaurant with id = {}.", id);
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant with id=" + id + " not found."));
    }

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

    public Meal getMealById(Integer id) {
        ValidatorUtil.checkNotNullId(id);
        logger.info("Restaurant Service layer: Returning meal with id = {}", id);
        return mealRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Meal with id=" + id + " not found.")) ;
    }

    public List<Meal> getAllMealsByRestaurantId(Integer id) {
        ValidatorUtil.checkNotNullId(id);
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Restaurant with id=" + id + " not found."));
        logger.info("Restaurant Service layer: Returning all meals for restaurant with id = {}.", id);
        return restaurant.getMeals();
    }

    // Delete Methods ------------------------------------------------------------

    public void deleteRestaurantById(Integer id) {
        ValidatorUtil.checkNotNullId(id);
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant with id=" + id + " not found."));
        restaurantRepository.delete(restaurant);
        restaurantRepository.flush();
        logger.info("Restaurant Service layer: Restaurant with id = {} has been removed.", id);
    }

    public void deleteAllRestaurants() {
        restaurantRepository.deleteAll();
        restaurantRepository.flush();
        logger.info("Restaurant Service layer: All restaurants have been removed.");
    }

    public void deleteMealById(Integer id) {
        ValidatorUtil.checkNotNullId(id);
        Meal meal = mealRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Meal with id=" + id + " not found."));
        Optional<Restaurant> restaurant = restaurantRepository.findById(meal.getRestaurant().getId());
        restaurant.get().removeMeal(meal);
        restaurantRepository.flush();
        logger.info("Restaurant Service layer: Meal with id = {} has been removed.", id);
    }

    public void deleteAllMeals() {
        List<Restaurant> restaurants = restaurantRepository.findAll();
        for (Restaurant restaurant : restaurants) {
            restaurant.removeAllMeals();
        }
        restaurantRepository.flush();
        logger.info("Restaurant Service layer: All meals have been removed.");
    }

    public void deleteAllMealsByRestaurantId(Integer id) {
        ValidatorUtil.checkNotNullId(id);
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant with id=" + id + " not found."));
        restaurant.removeAllMeals();
        restaurantRepository.flush();
    }

    // Create Methods -------------------------------------------------------------

    public Integer createRestaurant(Restaurant restaurant) {
        ValidatorUtil.checkNotNullInstance(restaurant);
        Integer id = restaurant.getId();
        ValidatorUtil.checkNullId(id);
        ValidatorUtil.checkNotNullProperties(restaurant);
        restaurantRepository.save(restaurant);
        logger.info("Restaurant Service layer: Creating new restaurant.");
        return restaurant.getId();
    }

    // Not Tested
    // All caches must be evicted before return
    public Integer createMealForRestaurantWithId(Integer id, Meal meal) {
        ValidatorUtil.checkNotNullId(id);
        ValidatorUtil.checkNotNullInstance(meal);
        ValidatorUtil.checkNotNullProperties(meal);
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Restaurant with id=" + id + " not found."));
        restaurant.getMeals().add(meal);
        restaurantRepository.flush();
        logger.info("Restaurant Service layer: Creating new meal for restaurant with id = {}.", id);
        return meal.getId();
    }

    // ----------------------------------------------------------------------

    // Update Methods -------------------------------------------------------

    // Tested
    // All caches must be evicted before return
    public void updateRestaurantById(Integer id, Restaurant restaurant) {
        ValidatorUtil.checkNotNullId(id);
        ValidatorUtil.checkNotNullInstance(restaurant);
        ValidatorUtil.checkNotNullProperties(restaurant);
        Restaurant r = restaurantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant with id=" + id + " not found."));

        r.setName(restaurant.getName());
        r.setVotes(restaurant.getVotes());
        r.getMeals().clear();
        r.getMeals().addAll(restaurant.getMeals());
        // The invocation of flush() below is mandatory for successful passing of "updateRestaurantById_withExistingName" test
        logger.info("Restaurant Service layer: Updating restaurant with id = {}.", id);
        restaurantRepository.flush();
    }

    // Not Tested
    // All caches must be evicted before return
    public void updateMealById(Integer id, Meal meal) {
        ValidatorUtil.checkNotNullId(id);
        ValidatorUtil.checkNotNullInstance(meal);
        ValidatorUtil.checkNotNullProperties(meal);
        Meal m = mealRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Meal with id=" + id + " not found."));
        m.setName(meal.getName());
        m.setCategory(meal.getCategory());
        m.setPrice(meal.getPrice());
        // The invocation of flush() below is mandatory for successful passing of "updateMealById_withExistingNameAndRestaurant" test
        logger.info("Restaurant Service layer: Updating meal with id = {}.", id);
        mealRepository.flush();
    }

    // ----------------------------------------------------------------------

}
