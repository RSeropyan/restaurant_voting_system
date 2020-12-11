package app.service;

import app.dao.MealRepository;
import app.dao.RestaurantRepository;
import app.entity.Meal;
import app.entity.Restaurant;
import app.exceptions.EntityNotFoundException;
import app.service.utils.ValidatorUtil;
import app.service.utils.RestaurantSorter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@CacheConfig(cacheNames = {"restaurants", "meals"})
public class RestaurantService {

    private final Logger logger = LoggerFactory.getLogger(app.service.RestaurantService.class);

    private static final Integer DEFAULT_CURRENT_PAGE = 0;
    private static final Integer DEFAULT_PAGE_SIZE = Integer.MAX_VALUE;
    private static final RestaurantSorter DEFAULT_SORTED_BY = RestaurantSorter.VOTES;
    private static final Sort.Direction DEFAULT_SORT_DIRECTION = Sort.Direction.DESC;

    private final RestaurantRepository restaurantRepository;
    private final MealRepository mealRepository;

    public RestaurantService(RestaurantRepository restaurantRepository, MealRepository mealRepository) {
        this.restaurantRepository = restaurantRepository;
        this.mealRepository = mealRepository;
    }

    // Retrieve Methods ----------------------------------------------------------

    // Tested
    public Restaurant getRestaurantById(Integer id) {
        ValidatorUtil.checkNotNullId(id);
        logger.info("Restaurant Service layer: Returning restaurant with id = {}.", id);
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant with id=" + id + " not found."));
    }

    // Tested
    // Must be @Cacheable
    public List<Restaurant> getAllRestaurants(
            @Nullable Integer currentPage,
            @Nullable Integer pageSize,
            @Nullable RestaurantSorter sorter,
            @Nullable Sort.Direction sortDirection) {

        currentPage   = (currentPage == null    ? DEFAULT_CURRENT_PAGE             : currentPage);
        pageSize      = (pageSize == null       ? DEFAULT_PAGE_SIZE                : pageSize);
        sortDirection = (sortDirection == null) ? DEFAULT_SORT_DIRECTION           : sortDirection;
        String sortBy = (sorter == null)        ? DEFAULT_SORTED_BY.getFieldName() : sorter.getFieldName();

        Sort sort = Sort.by(sortDirection, sortBy);
        Pageable pageable = PageRequest.of(currentPage, pageSize, sort);
        logger.info("Restaurant Service layer: Returning all restaurants.");
        return restaurantRepository.findAll(pageable).getContent();
    }

    // Not Tested
    public Meal getMealById(Integer id) {
        logger.info("Restaurant Service layer: Returning meal with id = {}", id);
        return mealRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Meal with id=" + id + " not found.")) ;
    }

    // Not Tested
    public List<Meal> getAllMealsByRestaurantId(Integer id) {
        ValidatorUtil.checkNotNullId(id);
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Restaurant with id=" + id + " not found."));
        logger.info("Restaurant Service layer: Returning all meals for restaurant with id = {}.", id);
        return restaurant.getMeals();
    }

    // ----------------------------------------------------------------------

    // Delete Methods -------------------------------------------------------

    // Tested
    // All caches must be evicted before return
    public void deleteRestaurantById(Integer id) {
        ValidatorUtil.checkNotNullId(id);
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant with id=" + id + " not found."));
        restaurantRepository.delete(restaurant);
        logger.info("Restaurant Service layer: Restaurant with id = {} has been removed.", id);
    }

    // Not Tested
    // All caches must be evicted before return
    public void deleteAllRestaurants() {
        restaurantRepository.deleteAll();
        logger.info("Restaurant Service layer: All restaurants have been removed.");
    }

    // Not Tested
    // All caches must be evicted before return
    public void deleteMealById(Integer id) {
        ValidatorUtil.checkNotNullId(id);
        boolean exists = mealRepository.existsById(id);
        if (exists) {
            mealRepository.deleteById(id);
            logger.info("Restaurant Service layer: Meal with id = {} has been removed.", id);
        }
        else {
            throw new EntityNotFoundException("Meal with id=" + id + " not found.");
        }
    }

    // Not Tested
    // All caches must be evicted before return
    public void deleteAllMeals() {
        mealRepository.deleteAll();
        logger.info("Restaurant Service layer: All meals have been removed.");
    }

    // Not Tested
    // All caches must be evicted before return
    public void deleteAllMealsByRestaurantId(Integer id) {
        ValidatorUtil.checkNotNullId(id);
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant with id=" + id + " not found."));
        restaurant.setMeals(new ArrayList<>());
        restaurantRepository.flush();
    }

    // ----------------------------------------------------------------------

    // Create Methods -------------------------------------------------------

    // Tested
    // All caches must be evicted before return
    public Restaurant createRestaurant(Restaurant restaurant) {
        ValidatorUtil.checkNotNullInstance(restaurant);
        Integer id = restaurant.getId();
        ValidatorUtil.checkNullId(id);
        ValidatorUtil.checkNotNullProperties(restaurant);
        logger.info("Restaurant Service layer: Creating new restaurant.");
        return restaurantRepository.save(restaurant);
    }

    // Not Tested
    // All caches must be evicted before return
    public Restaurant createMealForRestaurantWithId(Integer id, Meal meal) {
        ValidatorUtil.checkNotNullId(id);
        ValidatorUtil.checkNotNullInstance(meal);
        ValidatorUtil.checkNotNullProperties(meal);
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Restaurant with id=" + id + " not found."));
        restaurant.getMeals().add(meal);
        restaurantRepository.flush();
        logger.info("Restaurant Service layer: Creating new meal for restaurant with id = {}.", id);
        return restaurant;
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
        mealRepository.flush();
    }

    // ----------------------------------------------------------------------

}
