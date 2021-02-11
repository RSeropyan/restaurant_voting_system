package app.service;

import app.dao.RestaurantRepository;
import app.entity.Restaurant;
import app.service.exceptions.EntityNotFoundException;
import app.service.validation.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RestaurantVotingService {

    private final Logger logger = LoggerFactory.getLogger(app.service.RestaurantVotingService.class);

    private final RestaurantRepository restaurantRepository;

    public RestaurantVotingService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public void voteForRestaurantById(Integer id) {
        ValidationUtil.checkNotNullEntityId(id);
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Restaurant with id=" + id + " not found."));
        restaurant.addVote();
        restaurantRepository.flush();
        logger.info("Restaurant Voting Service: Restaurant with id = {} has been voted.", id);
    }

    public void clearAllVotes() {
        List<Restaurant> restaurants = restaurantRepository.findAll();
        restaurants.forEach(restaurant -> restaurant.setVotes(0));
        restaurantRepository.flush();
        logger.info("Restaurant Voting Service: All votes have been cleared.");
    }

}
