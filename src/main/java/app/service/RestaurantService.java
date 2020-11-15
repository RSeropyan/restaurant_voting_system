package app.service;

import app.dao.RestaurantRepository;
import app.entity.Restaurant;
import app.exceptions.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@CacheConfig(cacheNames = {"restaurants"})
public class RestaurantService {

    private final Logger logger = LoggerFactory.getLogger(app.service.RestaurantService.class);

    private final RestaurantRepository restaurantRepository;

    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Cacheable
    public Restaurant getById(Integer id) {
        logger.info("Returning restaurant with id = {}", id);

        return restaurantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant with id=" + id + " not found.")) ;
    }

    @Cacheable
    public Integer getTotalNumber() {
        return restaurantRepository.findAll().size();
    }

    @Cacheable
    public List<Restaurant> getAll(Integer currentPage, Integer pageSize,Sort sort) {
        logger.info("Returning all restaurants");

        currentPage = (currentPage == null ? 0 : currentPage);
        pageSize = (pageSize == null ? Integer.MAX_VALUE : pageSize);
        sort = (sort == null ? Sort.by("votes").descending() : sort);
        Pageable pageable = PageRequest.of(currentPage, pageSize, sort);

        return restaurantRepository.findAll(pageable).getContent();
    }

}
