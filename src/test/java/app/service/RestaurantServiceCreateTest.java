package app.service;

import app.entity.Meal;
import app.entity.MealCategory;
import app.entity.Restaurant;
import app.service.exceptions.EntityNotFoundException;
import app.service.exceptions.EntityPropertiesValidationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.List;

import static app.service.validation.ValidationUtil.*;
import static app.testdata.TestData.*;
import static org.assertj.core.api.Assertions.*;

public class RestaurantServiceCreateTest extends AbstractServiceTest {

    @Autowired
    private RestaurantService restaurantService;

    @Test
    public void createRestaurant() {
        Restaurant r1 = new Restaurant("Burger King", 0, new ArrayList<>());
        Meal m1 = new Meal("Georgian Salad", MealCategory.SALAD, 290);
        r1.addMeal(m1);

        Integer realRestaurantId = restaurantService.createRestaurant(r1);
        Restaurant realRestaurant = restaurantService.getRestaurantById(realRestaurantId);

        assertThat(realRestaurant)
                .usingRecursiveComparison()
                .ignoringFields("id", "meals")
                .isEqualTo(testRestaurant7);

        assertThat(realRestaurant.getMeals())
                .hasSameSizeAs(testRestaurant7.getMeals())
                .usingRecursiveComparison()
                .ignoringFields("id", "restaurant")
                .isEqualTo(testRestaurant7.getMeals());
    }

    @Test
    public void createRestaurant_withNullRestaurantInstance() {
        assertThatThrownBy(() -> restaurantService.createRestaurant(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ENTITY_INSTANCE_MUST_NOT_BE_NULL_MESSAGE);
    }

    @Test
    public void createRestaurant_withNotNullRestaurantId() {
        testRestaurant3.setId(0);
        assertThatThrownBy(() -> restaurantService.createRestaurant(testRestaurant3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ENTITY_ID_MUST_BE_NULL_MESSAGE);
    }

    @Test
    public void createRestaurant_withEmptyListOfMeals() {
        Integer realRestaurantId = restaurantService.createRestaurant(testRestaurant3);
        testRestaurant3.setId(realRestaurantId);
        assertThat(restaurantService.getRestaurantById(realRestaurantId))
                .usingRecursiveComparison()
                .isEqualTo(testRestaurant3);
    }

    @Test
    public void createRestaurant_withNullListOfMeals_andEmptyListOfMealsExpected() {
        Integer realRestaurantId = restaurantService.createRestaurant(testRestaurant4);
        assertThat(restaurantService.getRestaurantById(realRestaurantId).getMeals())
                .isNotNull()
                .hasSize(0);
    }

    @Test
    public void createRestaurant_withNonZeroVotes_andZeroVotesExpected() {
        Integer realRestaurantId = restaurantService.createRestaurant(testRestaurant5);
        assertThat(restaurantService.getRestaurantById(realRestaurantId).getMeals().size())
                .isZero();
    }

    @Test
    public void createRestaurant_withNullVotes_andZeroVotesExpected() {
        Integer realRestaurantId = restaurantService.createRestaurant(testRestaurant6);
        assertThat(restaurantService.getRestaurantById(realRestaurantId).getMeals().size())
                .isZero();
    }

    @Test
    public void createRestaurant_withBlankName() {
        testRestaurant3.setName("");
        Throwable thrown = catchThrowable(() -> restaurantService.createRestaurant(testRestaurant3));
        assertThat(thrown)
                .isInstanceOf(EntityPropertiesValidationException.class)
                .hasMessageContaining(ENTITY_PROPERTIES_NOT_VALID_MESSAGE);
        List<String> errors = ((EntityPropertiesValidationException) thrown).getErrors();
        assertThat(errors).hasSize(1);
        assertThat(errors.get(0)).isEqualTo("Restaurant name must not be blank.");
    }

    @Test
    public void createRestaurant_withExistingRestaurantName() {
        testRestaurant3.setName(testRestaurant1.getName());
        assertThatThrownBy(() -> restaurantService.createRestaurant(testRestaurant3))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    public void createMealForRestaurantWithId() {
        Integer realMealId = restaurantService.createMealForRestaurantWithId(1, new Meal("Margarita pizza", MealCategory.MAIN, 420));
        Meal realMeal = restaurantService.getMealById(realMealId);

        Meal testMeal = new Meal("Margarita pizza", MealCategory.MAIN, 420);
        testMeal.setRestaurant(new Restaurant());

        // Has new meal been correctly placed in Meal Table?
        assertThat(realMeal)
                .usingRecursiveComparison()
                .ignoringFields("id", "restaurant")
                .isEqualTo(testMeal);

        // Has restaurant id been correctly set for new meal?
        assertThat(realMeal.getRestaurant().getId()).isEqualTo(1);

        // Has new meal been correctly placed in restaurant meal list?
        Integer realMealRestaurantId = realMeal.getRestaurant().getId();
        List<Meal> realMeals = restaurantService.getRestaurantById(realMealRestaurantId).getMeals();
        assertThat(realMeals.contains(realMeal)).isTrue();
    }

    @Test
    public void createMealForRestaurantWithId_withNullRestaurantId() {
        assertThatThrownBy(() -> restaurantService.createMealForRestaurantWithId(null, new Meal("Margarita pizza", MealCategory.MAIN, 420)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ENTITY_ID_MUST_NOT_BE_NULL_MESSAGE);
    }


    @Test
    public void createMealForRestaurantWithId_withNullMealInstance() {
        assertThatThrownBy(() -> restaurantService.createMealForRestaurantWithId(1, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ENTITY_INSTANCE_MUST_NOT_BE_NULL_MESSAGE);
    }

    @Test
    public void createMealForRestaurantWithId_withNotNullMealId() {
        Meal meal = new Meal("Margarita pizza", MealCategory.MAIN, 420);
        meal.setId(100);
        assertThatThrownBy(() -> restaurantService.createMealForRestaurantWithId(1, meal))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ENTITY_ID_MUST_BE_NULL_MESSAGE);
    }

    @Test
    public void createMealForRestaurantWithId_withBlankName_withNotSelectedCategory_withNegativePrice() {
        Meal meal = new Meal("", null, -100);
        Throwable thrown = catchThrowable(() -> restaurantService.createMealForRestaurantWithId(1, meal));
        assertThat(thrown)
                .isInstanceOf(EntityPropertiesValidationException.class)
                .hasMessageContaining(ENTITY_PROPERTIES_NOT_VALID_MESSAGE);
        List<String> errors = ((EntityPropertiesValidationException) thrown).getErrors();
        assertThat(errors).hasSize(3);
        assertThat(errors.contains("Meal name must not be blank.")).isTrue();
        assertThat(errors.contains("Meal category must be selected.")).isTrue();
        assertThat(errors.contains("Meal price must be greater or equal to zero.")).isTrue();
    }

    @Test
    public void createMealForRestaurantWithId_withNonExistingRestaurantId() {
        Integer id = -1;
        assertThatThrownBy(() -> restaurantService.createMealForRestaurantWithId(id, new Meal("Margarita pizza", MealCategory.MAIN, 420)))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Restaurant with id=" + id + " not found.");
    }

    @Test
    public void createMealForRestaurantWithId_withExistingMealNameAndCategory() {
        Meal newMeal = testRestaurant1.getMeals().get(0);
        newMeal.setId(null);
        assertThatThrownBy(() -> restaurantService.createMealForRestaurantWithId(1, newMeal))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

}

