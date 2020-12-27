package app.service;

import app.entity.Restaurant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import static app.service.testdata.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class RestaurantServiceCreateTest extends AbstractServiceTest{

    @Autowired
    private RestaurantService restaurantService;

    @Test
    public void createRestaurant() {
        Integer realRestaurantId = restaurantService.createRestaurant(testNewRestaurant);
        testNewRestaurant.setId(realRestaurantId);
        assertThat(restaurantService.getRestaurantById(realRestaurantId))
                .usingRecursiveComparison()
                .isEqualTo(testNewRestaurant);
    }

    @Test
    public void createRestaurant_withExistingName() {
        testNewRestaurant.setName(testRestaurant1.getName());
        assertThatThrownBy(() -> restaurantService.createRestaurant(testNewRestaurant))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    public void createRestaurant_withNullEntity() {
        assertThatThrownBy(() -> restaurantService.createRestaurant(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The entity must not be null.");
    }

    @Test
    public void createRestaurant_withNotNullEntityId() {
        testNewRestaurant.setId(0);
        assertThatThrownBy(() -> restaurantService.createRestaurant(testNewRestaurant))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The entity id must be null.");
    }

    @Test
    public void createRestaurant_withNullProperties() {
        testNewRestaurant.setName(null);
        testNewRestaurant.setVotes(null);
        testNewRestaurant.setMeals(null);
        assertThatThrownBy(() -> restaurantService.createRestaurant(testNewRestaurant))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The entity's properties must not be null (except id and restaurant).");
    }

    @Test
    public void createMealForRestaurantWithId() {
        Integer realMealId = restaurantService.createMealForRestaurantWithId(1, newMeal);
        newMeal.setId(realMealId);
        newMeal.setRestaurant(restaurantService.getRestaurantById(1));
        assertThat(restaurantService.getMealById(realMealId))
                .usingRecursiveComparison()
                .isEqualTo(newMeal);
        Restaurant t = testRestaurant1;
//        assertThat(restaurantService.getRestaurantById(1))
//                .usingRecursiveComparison()
//                .isEqualTo(testRestaurant1);
    }

}
