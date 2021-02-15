package app.service;

import app.entity.Meal;
import app.entity.MealCategory;
import app.entity.Restaurant;
import app.service.exceptions.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

import static app.service.validation.ValidationUtil.*;
import static app.testdata.TestData.testRestaurant3;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class RestaurantServiceUpdateTest extends AbstractServiceTest {

    @Autowired
    private RestaurantService restaurantService;

    @Test
    public void updateRestaurantById() {
        restaurantService.updateRestaurantById(2, testRestaurant3);
        Restaurant realRestaurant = restaurantService.getRestaurantById(2);
        assertThat(realRestaurant)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(testRestaurant3);
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
                .hasMessageContaining(MESSAGE_checkNotNullId);
    }

    @Test
    public void updateRestaurantById_withNullRestaurantInstance() {
        assertThatThrownBy(() -> restaurantService.updateRestaurantById(1, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(MESSAGE_checkNotNullInstance);
    }

    @Test
    public void updateRestaurantById_withNullRestaurantProperties() {
        testRestaurant3.setName(null);
        testRestaurant3.setVotes(null);
        testRestaurant3.setMeals(null);
        assertThatThrownBy(() -> restaurantService.updateRestaurantById(1, testRestaurant3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(MESSAGE_checkNotNullRestaurantProperties);
    }

    @Test
    public void updateRestaurantById_withExistingRestaurantName() {
        Restaurant realRestaurant = restaurantService.getRestaurantById(1);
        testRestaurant3.setName(realRestaurant.getName());
        assertThatThrownBy(() -> restaurantService.updateRestaurantById(2, testRestaurant3))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    public void updateRestaurantById_withDuplicateMeals() {
        testRestaurant3.addMeal(new Meal("Ceaser Salad", MealCategory.SALAD, 350));
        testRestaurant3.addMeal(new Meal("Ceaser Salad", MealCategory.SALAD, 350));
        assertThatThrownBy(() -> restaurantService.updateRestaurantById(1, testRestaurant3))
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
                .hasMessageContaining(MESSAGE_checkNotNullId);
    }

    @Test
    public void updateMealById_withNullMealInstance() {
        assertThatThrownBy(() -> restaurantService.updateMealById(1, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(MESSAGE_checkNotNullInstance);
    }

    @Test
    public void updateMealById_withNullMealProperties() {
        assertThatThrownBy(() -> restaurantService.updateMealById(1, new Meal(null, null, null)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(MESSAGE_checkNotNullMealProperties);
    }

    @Test
    public void updateMealById_withExistingMealNameAndCategory() {
        Meal realMeal = restaurantService.getMealById(1);
        Meal testMeal = new Meal(realMeal.getName(), realMeal.getCategory(), realMeal.getPrice());
        assertThatThrownBy(() -> restaurantService.updateMealById(2, testMeal))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

}
