package app.service;

import app.dao.RestaurantRepository;
import app.entity.Restaurant;
import app.exceptions.EntityNotFoundException;
import app.service.utils.EntityValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class VotingService {

    private final RestaurantRepository restaurantRepository;

    public VotingService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public void voteById(Integer id) {
        EntityValidator.checkNotNullId(id);
        Restaurant restaurant = restaurantRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Restaurant with id=" + id + " not found."));
        Integer currentNumberOfVotes = restaurant.getVotes();
        restaurant.setVotes(++currentNumberOfVotes);
    }

}
