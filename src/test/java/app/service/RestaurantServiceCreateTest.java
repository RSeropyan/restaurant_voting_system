package app.service;

import app.entity.Meal;
import app.entity.MealCategory;
import app.exceptions.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

import static app.service.testdata.TestData.testRestaurant1;
import static app.service.testdata.TestData.testRestaurant3;
import static app.service.utils.ValidationUtil.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class RestaurantServiceCreateTest extends AbstractServiceTest {

    @Autowired
    private RestaurantService restaurantService;

    @Test
    public void createRestaurant() {
        Integer realRestaurantId = restaurantService.createRestaurant(testRestaurant3);
        testRestaurant3.setId(realRestaurantId);
        assertThat(restaurantService.getRestaurantById(realRestaurantId))
                .usingRecursiveComparison()
                .isEqualTo(testRestaurant3);
    }

    @Test
    public void createRestaurant_withExistingRestaurantName() {
        testRestaurant3.setName(testRestaurant1.getName());
        assertThatThrownBy(() -> restaurantService.createRestaurant(testRestaurant3))
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
        testRestaurant3.setId(0);
        assertThatThrownBy(() -> restaurantService.createRestaurant(testRestaurant3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(MESSAGE_checkNullId);
    }

    @Test
    public void createRestaurant_withNullRestaurantProperties() {
        testRestaurant3.setName(null);
        testRestaurant3.setVotes(null);
        assertThatThrownBy(() -> restaurantService.createRestaurant(testRestaurant3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(MESSAGE_checkNotNullProperties);
    }

    @Test
    public void createMealForRestaurantWithId() {
        Integer realMealId = restaurantService.createMealForRestaurantWithId(1, new Meal("Margarita pizza", MealCategory.MAIN, 420));
        Meal testMeal = new Meal("Margarita pizza", MealCategory.MAIN, 420);
        Meal realMeal = restaurantService.getMealById(realMealId);
        // Has new meal been correctly placed in Meal Table?
        assertThat(realMeal)
                .usingRecursiveComparison()
                .ignoringFields("id", "restaurant")
                .isEqualTo(testMeal);
        // Has restaurant id been correctly set for new meal
        assertThat(realMeal.getRestaurant().getId()).isEqualTo(1);
        // Has new meal been correctly placed in restaurant meal list?
        Integer realMealRestaurantId = realMeal.getRestaurant().getId();
        List<Meal> restaurantRealMeals = restaurantService.getAllMealsByRestaurantId(realMealRestaurantId);
        assertThat(restaurantRealMeals.contains(realMeal)).isTrue();
    }

    @Test
    public void createMealForRestaurantWithId_withNullRestaurantId() {
        assertThatThrownBy(() -> restaurantService.createMealForRestaurantWithId(null, new Meal("Margarita pizza", MealCategory.MAIN, 420)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(MESSAGE_checkNotNullId);
    }

    @Test
    public void createMealForRestaurantWithId_withNonExistingRestaurantId() {
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
        Meal newMeal = testRestaurant1.getMeals().get(0);
        newMeal.setId(null);
        assertThatThrownBy(() -> restaurantService.createMealForRestaurantWithId(1, newMeal))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

}

