package app.service;

import app.entity.Restaurant;
import app.service.exceptions.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static app.service.validation.ValidationUtil.ENTITY_ID_MUST_NOT_BE_NULL_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class RestaurantVotingServiceTest extends AbstractServiceTest {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private RestaurantVotingService restaurantVotingService;

    @Test
    public void voteForRestaurantById() {
        Restaurant restaurant = restaurantService.getRestaurantById(1);
        Integer initialNumberOfVotes = restaurant.getVotes();
        restaurantVotingService.voteForRestaurantById(1);
        restaurant = restaurantService.getRestaurantById(1);
        Integer incrementedNumberOfVotes = restaurant.getVotes();
        assertThat(++initialNumberOfVotes).isEqualTo(incrementedNumberOfVotes);
    }

    @Test
    public void voteForRestaurantById_withNullRestaurantId() {
        assertThatThrownBy(() -> restaurantVotingService.voteForRestaurantById(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ENTITY_ID_MUST_NOT_BE_NULL_MESSAGE);
    }

    @Test
    public void voteForRestaurantById_withNonExistingRestaurantId() {
        Integer id = -1;
        assertThatThrownBy(() -> restaurantVotingService.voteForRestaurantById(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Restaurant with id=" + id + " not found.");
    }

    @Test
    public void clearAllVotes() {
        List<Restaurant> r1 = restaurantService.getAllRestaurants(null);
        r1.forEach(restaurant -> {
            assertThat(restaurant.getVotes()).isNotZero();
        });
        restaurantVotingService.clearAllVotes();
        List<Restaurant> r2 = restaurantService.getAllRestaurants(null);
        r2.forEach(restaurant -> {
            assertThat(restaurant.getVotes()).isZero();
        });
    }

}
