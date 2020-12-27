package app.service;

import app.entity.Meal;
import app.entity.Restaurant;
import app.exceptions.EntityNotFoundException;
import app.service.utils.RestaurantSorter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;

import static app.service.testdata.TestData.*;
import static app.service.utils.PaginationSettings.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


public class RestaurantServiceGetTest extends AbstractServiceTest{

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private RestaurantVotingService restaurantVotingService;

    @Test
    public void getAllRestaurants_withNullPageableInstance() {
        List<Restaurant> testRestaurants = Arrays.asList(testRestaurant2, testRestaurant1);
        List<Restaurant> realRestaurants = restaurantService.getAllRestaurants(null);
        assertThat(realRestaurants).hasSameSizeAs(testRestaurants)
                .usingRecursiveComparison().isEqualTo(testRestaurants);
    }

    @Test
    public void getAllRestaurants_withSortedById() {
        List<Restaurant> testRestaurants = Arrays.asList(testRestaurant2, testRestaurant1);
        Pageable pageable = PageRequest.of(
                DEFAULT_CURRENT_PAGE,
                DEFAULT_PAGE_SIZE,
                Sort.by(DEFAULT_SORT_DIRECTION, RestaurantSorter.ID.getFieldName()));
        List<Restaurant> realRestaurants = restaurantService.getAllRestaurants(pageable);
        assertThat(realRestaurants).hasSameSizeAs(testRestaurants)
                .usingRecursiveComparison().isEqualTo(testRestaurants);
    }

    @Test
    public void getAllRestaurants_withSortedByVotesAscending() {
        List<Restaurant> testRestaurants = Arrays.asList(testRestaurant1, testRestaurant2);
        Pageable pageable = PageRequest.of(
                DEFAULT_CURRENT_PAGE,
                DEFAULT_PAGE_SIZE,
                Sort.by(Sort.Direction.ASC, RestaurantSorter.VOTES.getFieldName()));
        List<Restaurant> realRestaurants = restaurantService.getAllRestaurants(pageable);
        assertThat(realRestaurants).hasSameSizeAs(testRestaurants)
                .usingRecursiveComparison().isEqualTo(testRestaurants);
    }

    @Test
    public void getAllRestaurants_withSortedByVotesDescending() {
        List<Restaurant> testRestaurants = Arrays.asList(testRestaurant2, testRestaurant1);
        Pageable pageable = PageRequest.of(
                DEFAULT_CURRENT_PAGE,
                DEFAULT_PAGE_SIZE,
                Sort.by(Sort.Direction.DESC, RestaurantSorter.VOTES.getFieldName()));
        List<Restaurant> realRestaurants = restaurantService.getAllRestaurants(pageable);
        assertThat(realRestaurants).hasSameSizeAs(testRestaurants)
                .usingRecursiveComparison().isEqualTo(testRestaurants);
    }

    @Test
    public void getAllRestaurants_withSortedByNameAscending() {
        List<Restaurant> testRestaurants = Arrays.asList(testRestaurant1, testRestaurant2);
        Pageable pageable = PageRequest.of(
                DEFAULT_CURRENT_PAGE,
                DEFAULT_PAGE_SIZE,
                Sort.by(Sort.Direction.ASC, RestaurantSorter.NAME.getFieldName()));
        List<Restaurant> realRestaurants = restaurantService.getAllRestaurants(pageable);
        assertThat(realRestaurants).hasSameSizeAs(testRestaurants)
                .usingRecursiveComparison().isEqualTo(testRestaurants);
    }

    @Test
    public void getAllRestaurants_withSortedByNameDescending() {
        List<Restaurant> testRestaurants = Arrays.asList(testRestaurant2, testRestaurant1);
        Pageable pageable = PageRequest.of(
                DEFAULT_CURRENT_PAGE,
                DEFAULT_PAGE_SIZE,
                Sort.by(Sort.Direction.DESC, RestaurantSorter.NAME.getFieldName()));
        List<Restaurant> realRestaurants = restaurantService.getAllRestaurants(pageable);
        assertThat(realRestaurants).hasSameSizeAs(testRestaurants)
                .usingRecursiveComparison().isEqualTo(testRestaurants);
    }

    @Test
    public void getAllRestaurants_withExpectedFirstPage() {
        List<Restaurant> testRestaurants = Arrays.asList(testRestaurant2);
        Pageable pageable = PageRequest.of(
                DEFAULT_CURRENT_PAGE,
                1,
                Sort.by(DEFAULT_SORT_DIRECTION, DEFAULT_SORTED_BY.getFieldName()));
        List<Restaurant> realRestaurants = restaurantService.getAllRestaurants(pageable);
        assertThat(realRestaurants).hasSameSizeAs(testRestaurants)
                .usingRecursiveComparison().isEqualTo(testRestaurants);
    }

    @Test
    public void getAllRestaurants_withExpectedSecondPage() {
        List<Restaurant> testRestaurants = Arrays.asList(testRestaurant1);
        Pageable pageable = PageRequest.of(
                1,
                1,
                Sort.by(DEFAULT_SORT_DIRECTION, DEFAULT_SORTED_BY.getFieldName()));
        List<Restaurant> realRestaurants = restaurantService.getAllRestaurants(pageable);
        assertThat(realRestaurants).hasSameSizeAs(testRestaurants)
                .usingRecursiveComparison().isEqualTo(testRestaurants);
    }

    @Test
    public void getRestaurantById() {
        Restaurant realRestaurant1 = restaurantService.getRestaurantById(1);
        assertThat(realRestaurant1).usingRecursiveComparison().isEqualTo(testRestaurant1);
    }

    @Test
    public void getRestaurantById_withNonExistingId() {
        int id = -1;
        assertThatThrownBy(() -> restaurantService.getRestaurantById(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Restaurant with id=" + id + " not found.");
    }

    @Test
    public void getRestaurantById_withNullId() {
        assertThatThrownBy(() -> restaurantService.getRestaurantById(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The entity id must not be null.");
    }

    @Test
    public void getMealById() {
        Meal realMeal = restaurantService.getMealById(1);
        assertThat(realMeal).usingRecursiveComparison().isEqualTo(meal1);
    }

    @Test
    public void getMealById_withNonExistingId() {
        Integer id = -1;
        assertThatThrownBy(() -> restaurantService.getMealById(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Meal with id=" + id + " not found.");
    }

    @Test
    public void getMealById_withNullId() {
        assertThatThrownBy(() -> restaurantService.getMealById(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("The entity id must not be null.");
    }

    @Test
    public void getAllMealsByRestaurantId() {
        List<Meal> realMeals = restaurantService.getAllMealsByRestaurantId(1);
        assertThat(realMeals).usingRecursiveComparison().isEqualTo(testRestaurant1.getMeals());
    }

    @Test
    public void getAllMealsByRestaurantId_withNonExistingId() {
        Integer id = -1;
        assertThatThrownBy(() -> restaurantService.getAllMealsByRestaurantId(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Restaurant with id=" + id + " not found.");
    }

    @Test
    public void getAllMealsByRestaurantId_withNullId() {
        assertThatThrownBy(() -> restaurantService.getAllMealsByRestaurantId(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("The entity id must not be null.");
    }

}