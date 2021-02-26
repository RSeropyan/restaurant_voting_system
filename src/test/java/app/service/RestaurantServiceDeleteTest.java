package app.service;

import app.service.exceptions.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static app.service.validation.ValidationUtil.ENTITY_ID_MUST_NOT_BE_NULL_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class RestaurantServiceDeleteTest extends AbstractServiceTest{

    @Autowired
    private RestaurantService restaurantService;

    @Test
    public void deleteAllRestaurants() {
        restaurantService.deleteAllRestaurants();
        assertThat(restaurantService.getAllRestaurants(null)).hasSize(0);
    }

    @Test
    public void deleteRestaurantById() {
        Integer id = 1;
        restaurantService.deleteRestaurantById(id);
        assertThatThrownBy(() -> restaurantService.getRestaurantById(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Restaurant with id=" + id + " not found.");
    }

    @Test
    public void deleteRestaurantById_withNonExistingRestaurantId() {
        Integer id = -1;
        assertThatThrownBy(() -> restaurantService.deleteRestaurantById(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Restaurant with id=" + id + " not found.");
    }

    @Test
    public void deleteRestaurantById_withNullRestaurantId() {
        assertThatThrownBy(() -> restaurantService.deleteRestaurantById(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ENTITY_ID_MUST_NOT_BE_NULL_MESSAGE);
    }

    @Test
    public void deleteMealById() {
        Integer id = 1;
        restaurantService.deleteMealById(id);
        assertThatThrownBy(() -> restaurantService.getMealById(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Meal with id=" + id + " not found.");
    }

    @Test
    public void deleteMealById_withNonExistingMealId() {
        Integer id = -1;
        assertThatThrownBy(() -> restaurantService.deleteMealById(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Meal with id=" + id + " not found.");
    }

    @Test
    public void deleteMealById_withNullMealId() {
        assertThatThrownBy(() -> restaurantService.deleteMealById(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ENTITY_ID_MUST_NOT_BE_NULL_MESSAGE);
    }

    @Test
    public void deleteAllMeals() {
        restaurantService.deleteAllMeals();
        assertThat(restaurantService.getRestaurantById(1).getMeals()).hasSize(0);
        assertThat(restaurantService.getRestaurantById(2).getMeals()).hasSize(0);
    }

    @Test
    public void deleteAllMealsByRestaurantId() {
        restaurantService.deleteAllMealsForRestaurantWithId(1);
        assertThat(restaurantService.getRestaurantById(1).getMeals()).hasSize(0);
    }

    @Test
    public void deleteAllMealsByRestaurantId_withNonExistingRestaurantId() {
        Integer id = -1;
        assertThatThrownBy(() -> restaurantService.deleteAllMealsForRestaurantWithId(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Restaurant with id=" + id + " not found.");
    }

    @Test
    public void deleteAllMealsByRestaurantId_withNullRestaurantId() {
        assertThatThrownBy(() -> restaurantService.deleteAllMealsForRestaurantWithId(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ENTITY_ID_MUST_NOT_BE_NULL_MESSAGE);
    }

}
