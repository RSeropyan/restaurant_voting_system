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
import static app.testdata.TestData.testRestaurant3;
import static app.testdata.TestData.testRestaurant7;
import static org.assertj.core.api.Assertions.*;

public class RestaurantServiceUpdateTest extends AbstractServiceTest {

    @Autowired
    private RestaurantService restaurantService;

    @Test
    public void updateRestaurantById() {

        Restaurant r1 = new Restaurant("Burger King", 0, new ArrayList<>());
        Meal m1 = new Meal("Georgian Salad", MealCategory.SALAD, 290);
        r1.addMeal(m1);

        restaurantService.updateRestaurantById(2, r1);
        Restaurant realRestaurant = restaurantService.getRestaurantById(2);

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
    public void updateRestaurantById_withNonExistingRestaurantId() {
        Integer id = -1;
        assertThatThrownBy(() -> restaurantService.updateRestaurantById(id, testRestaurant3))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Restaurant with id=" + id + " not found.");
    }

    @Test
    public void updateRestaurantById_withNullRestaurantId() {
        assertThatThrownBy(() -> restaurantService.updateRestaurantById(null, testRestaurant3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ENTITY_ID_MUST_NOT_BE_NULL_MESSAGE);
    }

    @Test
    public void updateRestaurantById_withNullRestaurantInstance() {
        assertThatThrownBy(() -> restaurantService.updateRestaurantById(1, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ENTITY_INSTANCE_MUST_NOT_BE_NULL_MESSAGE);
    }

    @Test
    public void updateRestaurantById_withBlankName() {
        testRestaurant3.setName("");
        Throwable thrown = catchThrowable(() -> restaurantService.updateRestaurantById(1, testRestaurant3));
        assertThat(thrown)
                .isInstanceOf(EntityPropertiesValidationException.class)
                .hasMessageContaining(ENTITY_PROPERTIES_NOT_VALID_MESSAGE);
        List<String> errors = ((EntityPropertiesValidationException) thrown).getErrors();
        assertThat(errors).hasSize(1);
        assertThat(errors.contains("Restaurant name must not be blank.")).isTrue();
    }

    @Test
    public void updateRestaurantById_withIncorrectMeals() {
        Meal m1 = new Meal("", null, -350);
        Meal m2 = new Meal("", null, -350);
        testRestaurant3.addMeal(m1);
        testRestaurant3.addMeal(m2);
        Throwable thrown = catchThrowable(() -> restaurantService.updateRestaurantById(1, testRestaurant3));
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
    public void updateRestaurantById_withExistingRestaurantName() {
        Restaurant realRestaurant = restaurantService.getRestaurantById(1);
        testRestaurant3.setName(realRestaurant.getName());
        assertThatThrownBy(() -> restaurantService.updateRestaurantById(2, testRestaurant3))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    public void updateMealById() {
        restaurantService.updateMealById(1, new Meal("Greek Salad", MealCategory.SALAD, 310));
        Meal realMeal = restaurantService.getMealById(1);

        Meal testMeal = new Meal("Greek Salad", MealCategory.SALAD, 310);
        testMeal.setRestaurant(new Restaurant());

        // Has meal been correctly updated in Meal Table?
        assertThat(realMeal)
                .usingRecursiveComparison()
                .ignoringFields("id", "restaurant")
                .isEqualTo(testMeal);

        // Is restaurant id correct after update operation?
        assertThat(realMeal.getId()).isEqualTo(1);

        // Has meal been correctly updated in restaurant meal list?
        Integer realMealRestaurantId = realMeal.getRestaurant().getId();
        List<Meal> restaurantRealMeals = restaurantService.getRestaurantById(realMealRestaurantId).getMeals();
        assertThat(restaurantRealMeals.contains(realMeal)).isTrue();
    }

    @Test
    public void updateMealById_withNonExistingMealId() {
        Integer id = -1;
        assertThatThrownBy(() -> restaurantService.updateMealById(id, new Meal("Greek Salad", MealCategory.SALAD, 310)))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Meal with id=" + id + " not found.");
    }

    @Test
    public void updateMealById_withNullMealId() {
        assertThatThrownBy(() -> restaurantService.updateMealById(null, new Meal("Greek Salad", MealCategory.SALAD, 310)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ENTITY_ID_MUST_NOT_BE_NULL_MESSAGE);
    }

    @Test
    public void updateMealById_withNullMealInstance() {
        assertThatThrownBy(() -> restaurantService.updateMealById(1, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ENTITY_INSTANCE_MUST_NOT_BE_NULL_MESSAGE);
    }

    @Test
    public void updateMealById_withBlankName_withNotSelectedCategory_withNegativePrice() {
        Throwable thrown = catchThrowable(() -> restaurantService.updateMealById(1, new Meal("", null, -350)));
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
    public void updateMealById_withExistingMealNameAndCategory() {
        Meal realMeal = restaurantService.getMealById(1);
        Meal testMeal = new Meal(realMeal.getName(), realMeal.getCategory(), realMeal.getPrice());
        assertThatThrownBy(() -> restaurantService.updateMealById(2, testMeal))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

}
