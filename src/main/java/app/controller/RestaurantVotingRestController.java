package app.controller;

import app.service.RestaurantVotingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/v-service")
public class RestaurantVotingRestController {

    private final Logger logger = LoggerFactory.getLogger(app.controller.RestaurantVotingRestController.class);

    private final RestaurantVotingService restaurantVotingService;

    public RestaurantVotingRestController(RestaurantVotingService restaurantVotingService) {
        this.restaurantVotingService = restaurantVotingService;
    }

    @PutMapping("/restaurants/{id}")
    @ResponseStatus(HttpStatus.OK)
    @CacheEvict(cacheNames = "restaurantsCache", allEntries = true)
    // CacheEvict here is the weakest part of the app in terms of performance
    // because it makes usage of cache almost useless!
    public void voteForRestaurantById(@PathVariable Integer id) {
        restaurantVotingService.voteForRestaurantById(id);
        logger.info("Restaurant Voting Controller: voting for Restaurant with id = {}", id);
    }

    @DeleteMapping("/restaurants")
    @ResponseStatus(HttpStatus.OK)
    @CacheEvict(cacheNames = "restaurantsCache", allEntries = true)
    public void clearAllVotes() {
        restaurantVotingService.clearAllVotes();
        logger.info("Restaurant Voting Controller: All votes have been cleared.");
    }

}
