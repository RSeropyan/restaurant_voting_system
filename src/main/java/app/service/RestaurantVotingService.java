package app.service;

import app.dao.RestaurantRepository;
import app.entity.Restaurant;
import app.exceptions.EntityNotFoundException;
import app.service.utils.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RestaurantVotingService {

    private final Logger logger = LoggerFactory.getLogger(app.service.RestaurantVotingService.class);

    private final RestaurantRepository restaurantRepository;

    public RestaurantVotingService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public void voteForRestaurantById(Integer id) {
        ValidationUtil.checkNotNullId(id);
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Restaurant with id=" + id + " not found."));
        restaurant.addVote();
        logger.info("Restaurant Service layer: Restaurant with id = {} has been voted.", id);
    }

}
