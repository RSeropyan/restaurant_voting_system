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
import static org.assertj.core.api.Assertions.*;

public class RestaurantServiceCreateTest extends AbstractServiceTest {

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
    public void createRestaurant_withExistingRestaurantName() {
        testNewRestaurant.setName(testRestaurant1.getName());
        assertThatThrownBy(() -> restaurantService.createRestaurant(testNewRestaurant))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    public void createRestaurant_withNullRestaurantInstance() {
        assertThatThrownBy(() -> restaurantService.createRestaurant(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(MESSAGE_checkNotNullInstance);
    }

    @Test
    public void createRestaurant_withNotNullRestaurantId() {
        testNewRestaurant.setId(0);
        assertThatThrownBy(() -> restaurantService.createRestaurant(testNewRestaurant))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(MESSAGE_checkNullId);
    }

    @Test
    public void createRestaurant_withNullRestaurantProperties() {
        testNewRestaurant.setName(null);
        testNewRestaurant.setVotes(null);
        assertThatThrownBy(() -> restaurantService.createRestaurant(testNewRestaurant))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(MESSAGE_checkNotNullProperties);
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
                .hasMessageContaining(MESSAGE_checkNotNullId);
    }

    @Test
    public void createMealForRestaurantWithId_withNonExistingId() {
        Integer id = -1;
        assertThatThrownBy(() -> restaurantService.createMealForRestaurantWithId(id, new Meal("Margarita pizza", MealCategory.MAIN, 420)))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Restaurant with id=" + id + " not found.");
    }

    @Test
    public void createMealForRestaurantWithId_withNullMealInstance() {
        assertThatThrownBy(() -> restaurantService.createMealForRestaurantWithId(1, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(MESSAGE_checkNotNullInstance);
    }

    @Test
    public void createMealForRestaurantWithId_withNotNullMealId() {
        Meal meal = new Meal("Margarita pizza", MealCategory.MAIN, 420);
        meal.setId(100);
        assertThatThrownBy(() -> restaurantService.createMealForRestaurantWithId(1, meal))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(MESSAGE_checkNullId);
    }

    @Test
    public void createMealForRestaurantWithId_withNullMealProperties() {
        Meal meal = new Meal(null, null, null);
        assertThatThrownBy(() -> restaurantService.createMealForRestaurantWithId(1, meal))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(MESSAGE_checkNotNullProperties);
    }

    @Test
    public void createMealForRestaurantWithId_withExistingMealNameAndCategory() {
        meal1.setId(null);
        assertThatThrownBy(() -> restaurantService.createMealForRestaurantWithId(1, meal1))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

}

