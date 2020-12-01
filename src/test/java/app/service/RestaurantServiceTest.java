package app.service;

import app.entity.Restaurant;
import app.exceptions.EntityNotFoundException;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;

import static app.service.testdata.RestaurantTestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class RestaurantServiceTest extends ServiceTest {

    @Autowired
    private RestaurantService restaurantService;

    @Test
    public void getById() {
        Restaurant realRestaurant1 = restaurantService.getById(1);
        assertThat(realRestaurant1).usingRecursiveComparison().isEqualTo(testRestaurant1);
    }

    @Test
    public void getById_withNonExistingId() {
        int id = -1;
        assertThatThrownBy(() -> {
            restaurantService.getById(id);
        }).isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Restaurant with id=" + id + " not found.");
    }

    @Test
    public void getById_withNullId() {
        Integer id = null;
        assertThatThrownBy(() -> {
            restaurantService.getById(id);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The entity id must not be null.");
    }

    @Test
    public void getTotalNumber() {
        List<Restaurant> testRestaurants = Arrays.asList(testRestaurant1, testRestaurant2);
        Integer realTotalNumber = restaurantService.getTotalNumber();
        assertThat(realTotalNumber).isEqualTo(testRestaurants.size());
    }

    @Test
    public void getAll_NotPageableNotSorted() {
        List<Restaurant> testRestaurants = Arrays.asList(testRestaurant1, testRestaurant2);
        List<Restaurant> realRestaurants = restaurantService.getAll(null, null, Sort.unsorted());
        assertThat(realRestaurants).hasSameSizeAs(testRestaurants)
                .usingRecursiveComparison().isEqualTo(testRestaurants);
    }

    @Test
    public void getAll_SortedById() {
        List<Restaurant> testRestaurants = Arrays.asList(testRestaurant1, testRestaurant2);
        List<Restaurant> realRestaurants = restaurantService.getAll(null, null, Sort.by("id"));
        assertThat(realRestaurants).hasSameSizeAs(testRestaurants)
                .usingRecursiveComparison().isEqualTo(testRestaurants);
    }

    @Test
    public void getAll_SortedByVotesAscending() {
        List<Restaurant> testRestaurants = Arrays.asList(testRestaurant1, testRestaurant2);
        List<Restaurant> realRestaurants = restaurantService.getAll(null, null, Sort.by("votes").ascending());
        assertThat(realRestaurants).hasSameSizeAs(testRestaurants)
                .usingRecursiveComparison().isEqualTo(testRestaurants);
    }

    @Test
    public void getAll_SortedByVotesDescending() {
        List<Restaurant> testRestaurants = Arrays.asList(testRestaurant2, testRestaurant1);
        List<Restaurant> realRestaurants = restaurantService.getAll(null, null, Sort.by("votes").descending());
        assertThat(realRestaurants).hasSameSizeAs(testRestaurants)
                .usingRecursiveComparison().isEqualTo(testRestaurants);
    }

    @Test
    public void getAll_SortedByNameAscending() {
        List<Restaurant> testRestaurants = Arrays.asList(testRestaurant1, testRestaurant2);
        List<Restaurant> realRestaurants = restaurantService.getAll(null, null, Sort.by("name").ascending());
        assertThat(realRestaurants).hasSameSizeAs(testRestaurants)
                .usingRecursiveComparison().isEqualTo(testRestaurants);
    }

    @Test
    public void getAll_SortedByNameDescending() {
        List<Restaurant> testRestaurants = Arrays.asList(testRestaurant2, testRestaurant1);
        List<Restaurant> realRestaurants = restaurantService.getAll(null, null, Sort.by("name").descending());
        assertThat(realRestaurants).hasSameSizeAs(testRestaurants)
                .usingRecursiveComparison().isEqualTo(testRestaurants);
    }

    @Test
    public void getAll_expectedFirstPage() {
        List<Restaurant> testRestaurants = Arrays.asList(testRestaurant1);
        List<Restaurant> realRestaurants = restaurantService.getAll(0, 1, Sort.unsorted());
        assertThat(realRestaurants).hasSameSizeAs(testRestaurants)
                .usingRecursiveComparison().isEqualTo(testRestaurants);
    }

    @Test
    public void getAll_expectedSecondPage() {
        List<Restaurant> testRestaurants = Arrays.asList(testRestaurant2);
        List<Restaurant> realRestaurants = restaurantService.getAll(1, 1, Sort.unsorted());
        assertThat(realRestaurants).hasSameSizeAs(testRestaurants)
                .usingRecursiveComparison().isEqualTo(testRestaurants);
    }

    @Test
    public void deleteById() {
        List<Restaurant> testRestaurants = Arrays.asList(testRestaurant2);
        restaurantService.deleteById(1);
        List<Restaurant> realRestaurants = restaurantService.getAll(null, null, Sort.unsorted());
        assertThat(realRestaurants).hasSameSizeAs(testRestaurants)
                .usingRecursiveComparison().isEqualTo(testRestaurants);
    }

    @Test
    public void deleteById_withNonExistingId() {
        Integer id = -1;
        assertThatThrownBy(() -> {
            restaurantService.deleteById(id);
        }).isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Restaurant with id=" + id + " not found.");
    }

    @Test
    public void deleteById_withNullId() {
        Integer id = null;
        assertThatThrownBy(() -> {
            restaurantService.deleteById(id);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The entity id must not be null.");
    }

    @Test
    public void create() {
        Restaurant realRestaurant3 = restaurantService.create(testRestaurant3);
        assertThat(realRestaurant3).usingRecursiveComparison().isEqualTo(testRestaurant3);
    }

    @Test
    public void create_withExistingName() {
        assertThatThrownBy(() -> {
            restaurantService.create(new Restaurant(null, testRestaurant1.getName(), 10));
        }).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    public void create_withNullEntity() {
        assertThatThrownBy(() -> {
            restaurantService.create(null);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The entity must not be null.");
    }

    @Test
    public void create_withNotNullEntityId() {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(100);
        assertThatThrownBy(() -> {
            restaurantService.create(restaurant);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The entity must not contain not null value of id.");
    }

}