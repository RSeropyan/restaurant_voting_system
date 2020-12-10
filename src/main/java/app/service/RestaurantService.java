package app.service;

import app.dao.MealRepository;
import app.dao.RestaurantRepository;
import app.entity.Meal;
import app.entity.Restaurant;
import app.exceptions.EntityNotFoundException;
import app.service.utils.EntityValidator;
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

    // Get Methods ----------------------------------------------------------

    // Tested
    public Restaurant getRestaurantById(Integer id) {

        EntityValidator.checkNotNullId(id);
        logger.info("Restaurant Service layer: Returning restaurant with id = {}.", id);
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant with id=" + id + " not found."));

    }

    // Tested
    public List<Restaurant> getAllRestaurants(
            @Nullable Integer currentPage,
            @Nullable Integer pageSize,
            @Nullable RestaurantSorter sorter,
            @Nullable Sort.Direction sortDirection) {

        logger.info("Restaurant Service layer: Returning all restaurants.");

        currentPage   = (currentPage == null    ? DEFAULT_CURRENT_PAGE             : currentPage);
        pageSize      = (pageSize == null       ? DEFAULT_PAGE_SIZE                : pageSize);
        sortDirection = (sortDirection == null) ? DEFAULT_SORT_DIRECTION           : sortDirection;
        String sortBy = (sorter == null)        ? DEFAULT_SORTED_BY.getFieldName() : sorter.getFieldName();

        Sort sort = Sort.by(sortDirection, sortBy);
        Pageable pageable = PageRequest.of(currentPage, pageSize, sort);
        return restaurantRepository.findAll(pageable).getContent();
    }

    // Not Tested
    public Meal getMealById(Integer id) {
        logger.info("Meal Service layer: Returning meal with id = {}", id);

        return mealRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Meal with id=" + id + " not found.")) ;
    }

    // Not Tested
    public List<Meal> getAllMealsByRestaurantId(Integer id) {
        EntityValidator.checkNotNullId(id);
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Restaurant with id=" + id + " not found."));
        logger.info("Restaurant Service layer: Returning all meals for restaurant with id = {}.", id);
        return restaurant.getMeals();
    }

    // ----------------------------------------------------------------------

    public void deleteById(Integer id) {

        EntityValidator.checkNotNullId(id);
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant with id=" + id + " not found."));
        restaurantRepository.delete(restaurant);
        logger.info("Restaurant Service layer: Restaurant with id = {} has been removed.", id);

    }

    public Restaurant create(Restaurant restaurant) {

        EntityValidator.checkNotNullInstance(restaurant);
        Integer id = restaurant.getId();
        EntityValidator.checkNullId(id);
        EntityValidator.checkNotNullProperties(restaurant);

        logger.info("Restaurant Service layer: Creating new restaurant.");
        return restaurantRepository.save(restaurant);

    }

    public void updateById(Integer id, Restaurant restaurant) {

        EntityValidator.checkNotNullId(id);
        Restaurant r = restaurantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant with id=" + id + " not found."));
        EntityValidator.checkNotNullInstance(restaurant);
        EntityValidator.checkNotNullProperties(restaurant);

        r.setName(restaurant.getName());
        r.setVotes(restaurant.getVotes());
        r.getMeals().clear();
        r.getMeals().addAll(restaurant.getMeals());
        // The invocation of flush() below is mandatory for successful passing of "updateById_withExistingName" test
        restaurantRepository.flush();

    }

}
