package app.controller;

import app.service.RestaurantVotingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/v-service")
public class VotingRestController {

    private final Logger logger = LoggerFactory.getLogger(app.controller.VotingRestController.class);

    private final RestaurantVotingService restaurantVotingService;

    public VotingRestController(RestaurantVotingService restaurantVotingService) {
        this.restaurantVotingService = restaurantVotingService;
    }

    @PostMapping("/restaurants/{id}")
    public ResponseEntity voteById(@PathVariable Integer id) {
        logger.info("Controller layer: voting for Restaurant with id = {}", id);
        restaurantVotingService.voteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
