package app.service;

import app.entity.Restaurant;
import app.exceptions.EntityNotFoundException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;

import static app.service.testdata.RestaurantTestData.testRestaurant1;
import static app.service.testdata.RestaurantTestData.testRestaurant2;
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
    public void getByNonExistingId() {
        int id = -1;
        assertThatThrownBy(() -> {
            restaurantService.getById(id);
        }).isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Restaurant with id=" + id + " not found.");
    }

    @Test
    public void getTotalNumber() {
        List<Restaurant> testRestaurants = Arrays.asList(testRestaurant1, testRestaurant2);
        List<Restaurant> realRestaurants = restaurantService.getAll(null, null, Sort.unsorted());
        assertThat(realRestaurants).hasSameSizeAs(testRestaurants);
    }

    @Test
    public void getAllNotPageableNotSorted() {
        List<Restaurant> testRestaurants = Arrays.asList(testRestaurant1, testRestaurant2);
        List<Restaurant> realRestaurants = restaurantService.getAll(null, null, Sort.unsorted());
        assertThat(realRestaurants).hasSameSizeAs(testRestaurants)
                .usingRecursiveComparison().isEqualTo(testRestaurants);
    }

    @Test
    public void getAllSortedById() {
        List<Restaurant> testRestaurants = Arrays.asList(testRestaurant1, testRestaurant2);
        List<Restaurant> realRestaurants = restaurantService.getAll(null, null, Sort.by("id"));
        assertThat(realRestaurants).hasSameSizeAs(testRestaurants)
                .usingRecursiveComparison().isEqualTo(testRestaurants);
    }

    @Test
    public void getAllSortedByVotesAscending() {
        List<Restaurant> testRestaurants = Arrays.asList(testRestaurant1, testRestaurant2);
        List<Restaurant> realRestaurants = restaurantService.getAll(null, null, Sort.by("votes").ascending());
        assertThat(realRestaurants).hasSameSizeAs(testRestaurants)
                .usingRecursiveComparison().isEqualTo(testRestaurants);
    }

    @Test
    public void getAllSortedByVotesDescending() {
        List<Restaurant> testRestaurants = Arrays.asList(testRestaurant2, testRestaurant1);
        List<Restaurant> realRestaurants = restaurantService.getAll(null, null, Sort.by("votes").descending());
        assertThat(realRestaurants).hasSameSizeAs(testRestaurants)
                .usingRecursiveComparison().isEqualTo(testRestaurants);
    }

    @Test
    public void getAllSortedByNameAscending() {
        List<Restaurant> testRestaurants = Arrays.asList(testRestaurant1, testRestaurant2);
        List<Restaurant> realRestaurants = restaurantService.getAll(null, null, Sort.by("name").ascending());
        assertThat(realRestaurants).hasSameSizeAs(testRestaurants)
                .usingRecursiveComparison().isEqualTo(testRestaurants);
    }

    @Test
    public void getAllSortedByNameDescending() {
        List<Restaurant> testRestaurants = Arrays.asList(testRestaurant2, testRestaurant1);
        List<Restaurant> realRestaurants = restaurantService.getAll(null, null, Sort.by("name").descending());
        assertThat(realRestaurants).hasSameSizeAs(testRestaurants)
                .usingRecursiveComparison().isEqualTo(testRestaurants);
    }

    @Test
    public void getAllFirstPage() {
        List<Restaurant> testRestaurants = Arrays.asList(testRestaurant1);
        List<Restaurant> realRestaurants = restaurantService.getAll(0, 1, Sort.unsorted());
        assertThat(realRestaurants).hasSameSizeAs(testRestaurants)
                .usingRecursiveComparison().isEqualTo(testRestaurants);
    }

    @Test
    public void getAllSecondPage() {
        List<Restaurant> testRestaurants = Arrays.asList(testRestaurant2);
        List<Restaurant> realRestaurants = restaurantService.getAll(1, 1, Sort.unsorted());
        assertThat(realRestaurants).hasSameSizeAs(testRestaurants)
                .usingRecursiveComparison().isEqualTo(testRestaurants);
    }

}