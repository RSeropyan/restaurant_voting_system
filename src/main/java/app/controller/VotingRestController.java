package app.controller;

import app.service.RestaurantVotingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/v-service")
public class VotingRestController {

    private final Logger logger = LoggerFactory.getLogger(app.controller.VotingRestController.class);

    private final RestaurantVotingService restaurantVotingService;

    public VotingRestController(RestaurantVotingService restaurantVotingService) {
        this.restaurantVotingService = restaurantVotingService;
    }

    @PostMapping("/restaurants/{id}")
    @ResponseStatus(HttpStatus.OK)
    @CacheEvict(cacheNames = "restaurantsCache", allEntries = true)
    // CacheEvict here is the weakest part of the app in terms of performance
    // It makes usage of cache almost useless!
    public void voteForRestaurantById(@PathVariable Integer id) {
        restaurantVotingService.voteForRestaurantById(id);
        logger.info("Controller layer: voting for Restaurant with id = {}", id);
    }
}
