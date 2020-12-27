package app.service;

import app.dao.RestaurantRepository;
import app.entity.Restaurant;
import app.exceptions.EntityNotFoundException;
import app.service.utils.ValidationUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RestaurantVotingService {

    private final RestaurantRepository restaurantRepository;

    public RestaurantVotingService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public void voteForRestaurantById(Integer id) {
        ValidationUtil.checkNotNullId(id);
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Restaurant with id=" + id + " not found."));
        Integer currentNumberOfVotes = restaurant.getVotes();
        restaurant.setVotes(++currentNumberOfVotes);
    }

}
