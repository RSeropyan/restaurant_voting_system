package app.service;

import app.dao.RestaurantRepository;
import app.entity.Restaurant;
import app.exceptions.EntityNotFoundException;
import app.service.utils.EntityValidator;
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
@CacheConfig(cacheNames = {"restaurants"})
public class RestaurantService {

    private final Logger logger = LoggerFactory.getLogger(app.service.RestaurantService.class);

    private static final Integer DEFAULT_CURRENT_PAGE = 0;
    private static final Integer DEFAULT_PAGE_SIZE = Integer.MAX_VALUE;
    private static final String DEFAULT_PROPERTY_TO_SORT_BY = "votes";

    private final RestaurantRepository restaurantRepository;

    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public Restaurant getById(Integer id) {

        EntityValidator.checkNotNullId(id);
        logger.info("Restaurant Service layer: Returning restaurant with id = {}.", id);
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant with id=" + id + " not found."));

    }

    public Integer getTotalNumber() {
        logger.info("Restaurant Service layer: Returning total number of restaurants.");
        return restaurantRepository.findAll().size();
    }

    public List<Restaurant> getAll(@Nullable Integer currentPage, @Nullable Integer pageSize, @Nullable Sort sort) {
        logger.info("Restaurant Service layer: Returning all restaurants.");

        currentPage = (currentPage == null ? DEFAULT_CURRENT_PAGE : currentPage);
        pageSize = (pageSize == null ? DEFAULT_PAGE_SIZE : pageSize);
        sort = (sort == null ? Sort.by(DEFAULT_PROPERTY_TO_SORT_BY).descending() : sort);
        Pageable pageable = PageRequest.of(currentPage, pageSize, sort);

        return restaurantRepository.findAll(pageable).getContent();
    }

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
