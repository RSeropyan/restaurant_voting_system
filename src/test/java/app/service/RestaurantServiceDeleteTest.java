package app.service;

import app.entity.Restaurant;
import app.exceptions.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static app.service.testdata.TestData.testRestaurant2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class RestaurantServiceDeleteTest extends AbstractServiceTest{

    @Autowired
    private RestaurantService restaurantService;

    @Test
    public void deleteAllRestaurants() {
        restaurantService.deleteAllRestaurants();
        assertThat(restaurantService.getAllRestaurants(null).size()).isEqualTo(0);
    }

    @Test
    public void deleteRestaurantById() {
        List<Restaurant> testRestaurants = Arrays.asList(testRestaurant2);
        restaurantService.deleteRestaurantById(1);
        List<Restaurant> realRestaurants = restaurantService.getAllRestaurants(null);
        assertThat(realRestaurants)
                .hasSameSizeAs(testRestaurants)
                .usingRecursiveComparison().isEqualTo(testRestaurants);
    }

    @Test
    public void deleteRestaurantById_withNonExistingId() {
        Integer id = -1;
        assertThatThrownBy(() -> restaurantService.deleteRestaurantById(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Restaurant with id=" + id + " not found.");
    }

    @Test
    public void deleteRestaurantById_withNullId() {
        assertThatThrownBy(() -> restaurantService.deleteRestaurantById(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The entity id must not be null.");
    }

    @Test
    public void deleteMealById() {
        Integer id = 1;
        int initialNumberOfMeals = restaurantService.getAllMealsByRestaurantId(id).size();
        restaurantService.deleteMealById(id);
        assertThatThrownBy(() -> restaurantService.getMealById(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Meal with id=" + id + " not found.");
        assertThat(restaurantService.getAllMealsByRestaurantId(id).size()).isEqualTo(--initialNumberOfMeals);
    }

    @Test
    public void deleteMealById_withNonExistingId() {
        Integer id = -1;
        assertThatThrownBy(() -> restaurantService.deleteMealById(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Meal with id=" + id + " not found.");
    }

    @Test
    public void deleteMealById_withNullId() {
        assertThatThrownBy(() -> restaurantService.deleteMealById(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The entity id must not be null.");
    }

    @Test
    public void deleteAllMeals() {
        restaurantService.deleteAllMeals();
        assertThat(restaurantService.getAllMealsByRestaurantId(1).size()).isEqualTo(0);
        assertThat(restaurantService.getAllMealsByRestaurantId(2).size()).isEqualTo(0);
        Integer id = 1;
        assertThatThrownBy(() -> restaurantService.deleteMealById(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Meal with id=" + id + " not found.");
    }

    @Test
    public void deleteAllMealsByRestaurantId() {
        Integer id = 1;
        restaurantService.deleteAllMealsByRestaurantId(id);
        assertThat(restaurantService.getAllMealsByRestaurantId(id).size()).isEqualTo(0);
        assertThatThrownBy(() -> restaurantService.getMealById(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Meal with id=" + id + " not found.");
    }

    @Test
    public void deleteAllMealsByRestaurantId_withNonExistingId() {
        Integer id = -1;
        assertThatThrownBy(() -> restaurantService.deleteAllMealsByRestaurantId(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Restaurant with id=" + id + " not found.");
    }

    @Test
    public void deleteAllMealsByRestaurantId_withNullId() {
        assertThatThrownBy(() -> restaurantService.deleteAllMealsByRestaurantId(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The entity id must not be null.");
    }

}
