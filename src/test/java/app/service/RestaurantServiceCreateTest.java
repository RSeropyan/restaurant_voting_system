package app.service;

import app.entity.Meal;
import app.entity.MealCategory;
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
        assertThatThrownBy(() -> restaurantService.createRestaurant(testNewRestaurant))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The entity's properties must not be null (except id).");
    }

    @Test
    public void createMealForRestaurantWithId() {
        Integer realMealId = restaurantService.createMealForRestaurantWithId(1, new Meal("Margarita pizza", MealCategory.MAIN, 420));
        testNewMeal.setId(realMealId);
        testRestaurant1.addMeal(testNewMeal);
        Meal realMeal = restaurantService.getMealById(realMealId);
        assertThat(realMeal)
                .usingRecursiveComparison()
                .isEqualTo(testNewMeal);
        Restaurant realRestaurant = restaurantService.getRestaurantById(1);
        assertThat(realRestaurant)
                .usingRecursiveComparison()
                .isEqualTo(testRestaurant1);
    }

    @Test
    public void createMealForRestaurantWithId_withNullRestaurantId() {
        assertThatThrownBy(() -> restaurantService.createMealForRestaurantWithId(null, new Meal("Margarita pizza", MealCategory.MAIN, 420)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The entity id must not be null.");

    }



}
