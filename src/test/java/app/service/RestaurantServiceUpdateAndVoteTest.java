package app.service;

import app.entity.Meal;
import app.entity.MealCategory;
import app.entity.Restaurant;
import app.exceptions.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import static app.service.testdata.TestData.*;
import static app.service.utils.ValidationUtil.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class RestaurantServiceUpdateAndVoteTest extends AbstractServiceTest{

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private RestaurantVotingService restaurantVotingService;

    @Test
    public void updateRestaurantById() {
        restaurantService.updateRestaurantById(2, testRestaurant3);
        Restaurant realRestaurant = restaurantService.getRestaurantById(2);
        testRestaurant3.setId(realRestaurant.getId());
        assertThat(realRestaurant)
                .usingRecursiveComparison()
                .isEqualTo(testRestaurant3);
    }

    @Test
    public void updateRestaurantById_withNonExistingId() {
        Integer id = -1;
        assertThatThrownBy(() -> restaurantService.updateRestaurantById(id, testRestaurant3))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Restaurant with id=" + id + " not found.");
    }

    @Test
    public void updateRestaurantById_withExistingName() {
        testRestaurant3.setName(testRestaurant1.getName());
        assertThatThrownBy( () -> restaurantService.updateRestaurantById(2, testRestaurant3))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    public void updateRestaurantById_withNullEntity() {
        assertThatThrownBy(() -> restaurantService.updateRestaurantById(1, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(MESSAGE_checkNotNullInstance);
    }

    @Test
    public void updateRestaurantById_withNullEntityId() {
        assertThatThrownBy(() -> restaurantService.updateRestaurantById(null, testRestaurant3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(MESSAGE_checkNotNullId);
    }

    @Test
    public void updateRestaurantById_withNullProperties() {
        testRestaurant3.setName(null);
        testRestaurant3.setVotes(null);
        testRestaurant3.setMeals(null);
        assertThatThrownBy(() -> restaurantService.updateRestaurantById(1, testRestaurant3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(MESSAGE_checkNotNullProperties);
    }

    @Test
    public void updateRestaurantById_withDuplicateMeals() {
        testRestaurant3.addMeal(new Meal("Ceaser Salad", MealCategory.SALAD, 350));
        testRestaurant3.addMeal(new Meal("Ceaser Salad", MealCategory.SALAD, 350));
        assertThatThrownBy(() -> restaurantService.updateRestaurantById(1, testRestaurant3))
                .isInstanceOf(DataIntegrityViolationException.class);
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
