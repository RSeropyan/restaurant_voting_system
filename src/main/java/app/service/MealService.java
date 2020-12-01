package app.service;

import app.dao.MealRepository;
import app.entity.Meal;
import app.exceptions.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@CacheConfig(cacheNames = {"meals"})
public class MealService {

    private final Logger logger = LoggerFactory.getLogger(app.service.MealService.class);

    private final MealRepository mealRepository;

    public MealService(MealRepository mealRepository) {
        this.mealRepository = mealRepository;
    }

    public Meal getById(Integer id) {
        logger.info("Meal Service layer: Returning meal with id = {}", id);

        return mealRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Meal with id=" + id + " not found.")) ;
    }

}
