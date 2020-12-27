package app.service;

import app.entity.Restaurant;
import app.exceptions.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import static app.service.testdata.TestData.testNewRestaurant;
import static app.service.testdata.TestData.testRestaurant1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class RestaurantServiceUpdateTest extends AbstractServiceTest{

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private RestaurantVotingService restaurantVotingService;

    @Test
    public void updateRestaurantById() {
        restaurantService.updateRestaurantById(2, testNewRestaurant);
        Restaurant realRestaurant = restaurantService.getRestaurantById(2);
        testNewRestaurant.setId(realRestaurant.getId());
        assertThat(realRestaurant).usingRecursiveComparison().isEqualTo(testNewRestaurant);
    }

    @Test
    public void updateRestaurantById_withNonExistingId() {
        Integer id = -1;
        assertThatThrownBy(() -> restaurantService.updateRestaurantById(id, testRestaurant1))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Restaurant with id=" + id + " not found.");
    }

    @Test
    public void updateRestaurantById_withExistingName() {
        testNewRestaurant.setName(testRestaurant1.getName());
        assertThatThrownBy( () -> restaurantService.updateRestaurantById(2, testNewRestaurant))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    public void updateRestaurantById_withNullEntity() {
        assertThatThrownBy(() -> restaurantService.updateRestaurantById(1, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The entity must not be null.");
    }

    @Test
    public void updateRestaurantById_withNullEntityId() {
        assertThatThrownBy(() -> { restaurantService.updateRestaurantById(null, testNewRestaurant); })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The entity id must not be null.");
    }

    @Test
    public void updateRestaurantById_withNullProperties() {
        testNewRestaurant.setName(null);
        testNewRestaurant.setVotes(null);
        testNewRestaurant.setMeals(null);
        assertThatThrownBy(() -> { restaurantService.updateRestaurantById(1, testNewRestaurant); })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The entity's properties must not be null (except id and restaurant).");
    }

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
    public void voteForRestaurantById_withNullId() {
        assertThatThrownBy(() -> restaurantVotingService.voteForRestaurantById(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The entity id must not be null.");
    }

    @Test
    public void voteForRestaurantById_withNonExistingId() {
        Integer id = -1;
        assertThatThrownBy(() -> restaurantVotingService.voteForRestaurantById(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Restaurant with id=" + id + " not found.");
    }

}
