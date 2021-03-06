package app.service;

import app.entity.Meal;
import app.entity.Restaurant;
import app.service.exceptions.EntityNotFoundException;
import app.service.helpers.RestaurantSorter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static app.service.RestaurantService.*;
import static app.service.validation.ValidationUtil.ENTITY_ID_MUST_NOT_BE_NULL_MESSAGE;
import static app.testdata.TestData.testRestaurant1;
import static app.testdata.TestData.testRestaurant2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
public class RestaurantServiceGetTest extends AbstractServiceTest{

    @Autowired
    private RestaurantService restaurantService;

    @Test
    public void getAllRestaurants_withNullListView_withNullPagination() {
        List<Restaurant> testRestaurants = Arrays.asList(testRestaurant2, testRestaurant1);
        List<Restaurant> realRestaurants = restaurantService.getAllRestaurants(null, null);
        assertThat(realRestaurants)
                .hasSameSizeAs(testRestaurants)
                .usingRecursiveComparison()
                .isEqualTo(testRestaurants);
    }

    @Test
    public void getAllRestaurants_withShortListView_withNullPagination() {
        List<Restaurant> testRestaurants = Arrays.asList(testRestaurant2, testRestaurant1);
        List<Restaurant> realRestaurants = restaurantService.getAllRestaurants(ListView.SHORT, null);
        assertThat(realRestaurants)
                .hasSameSizeAs(testRestaurants)
                .usingRecursiveComparison()
                .isEqualTo(testRestaurants);
    }

    @Test
    public void getAllRestaurants_withDetailedListView_withNullPagination() {
        List<Restaurant> testRestaurants = Arrays.asList(testRestaurant2, testRestaurant1);
        List<Restaurant> realRestaurants = restaurantService.getAllRestaurants(ListView.DETAILED, null);
        assertThat(realRestaurants)
                .hasSameSizeAs(testRestaurants)
                .usingRecursiveComparison()
                .isEqualTo(testRestaurants);
    }

    @Test
    public void getAllRestaurants_withNullListView_sortedById_Ascending() {
        List<Restaurant> testRestaurants = Arrays.asList(testRestaurant1, testRestaurant2);
        Pageable pageable = PageRequest.of(
                DEFAULT_CURRENT_PAGE, DEFAULT_PAGE_SIZE,
                Sort.by(Sort.Direction.ASC, RestaurantSorter.ID.getFieldName()));
        List<Restaurant> realRestaurants = restaurantService.getAllRestaurants(null, pageable);
        assertThat(realRestaurants)
                .hasSameSizeAs(testRestaurants)
                .usingRecursiveComparison()
                .isEqualTo(testRestaurants);
    }

    @Test
    public void getAllRestaurants_withNullListView_sortedById_Descending() {
        List<Restaurant> testRestaurants = Arrays.asList(testRestaurant2, testRestaurant1);
        Pageable pageable = PageRequest.of(
                DEFAULT_CURRENT_PAGE, DEFAULT_PAGE_SIZE,
                Sort.by(Sort.Direction.DESC, RestaurantSorter.ID.getFieldName()));
        List<Restaurant> realRestaurants = restaurantService.getAllRestaurants(null, pageable);
        assertThat(realRestaurants)
                .hasSameSizeAs(testRestaurants)
                .usingRecursiveComparison()
                .isEqualTo(testRestaurants);
    }

    @Test
    public void getAllRestaurants_withNullListView_sortedByName_Ascending() {
        List<Restaurant> testRestaurants = Arrays.asList(testRestaurant1, testRestaurant2);
        Pageable pageable = PageRequest.of(
                DEFAULT_CURRENT_PAGE, DEFAULT_PAGE_SIZE,
                Sort.by(Sort.Direction.ASC, RestaurantSorter.NAME.getFieldName()));
        List<Restaurant> realRestaurants = restaurantService.getAllRestaurants(null, pageable);
        assertThat(realRestaurants)
                .hasSameSizeAs(testRestaurants)
                .usingRecursiveComparison()
                .isEqualTo(testRestaurants);
    }

    @Test
    public void getAllRestaurants_withNullListView_sortedByName_Descending() {
        List<Restaurant> testRestaurants = Arrays.asList(testRestaurant2, testRestaurant1);
        Pageable pageable = PageRequest.of(
                DEFAULT_CURRENT_PAGE, DEFAULT_PAGE_SIZE,
                Sort.by(Sort.Direction.DESC, RestaurantSorter.NAME.getFieldName()));
        List<Restaurant> realRestaurants = restaurantService.getAllRestaurants(null, pageable);
        assertThat(realRestaurants)
                .hasSameSizeAs(testRestaurants)
                .usingRecursiveComparison()
                .isEqualTo(testRestaurants);
    }

    @Test
    public void getAllRestaurants_withNullListView_sortedByVotes_Ascending() {
        List<Restaurant> testRestaurants = Arrays.asList(testRestaurant1, testRestaurant2);
        Pageable pageable = PageRequest.of(
                DEFAULT_CURRENT_PAGE, DEFAULT_PAGE_SIZE,
                Sort.by(Sort.Direction.ASC, RestaurantSorter.VOTES.getFieldName()));
        List<Restaurant> realRestaurants = restaurantService.getAllRestaurants(null, pageable);
        assertThat(realRestaurants)
                .hasSameSizeAs(testRestaurants)
                .usingRecursiveComparison()
                .isEqualTo(testRestaurants);
    }

    @Test
    public void getAllRestaurants_withNullListView_sortedByVotes_Descending() {
        List<Restaurant> testRestaurants = Arrays.asList(testRestaurant2, testRestaurant1);
        Pageable pageable = PageRequest.of(
                DEFAULT_CURRENT_PAGE, DEFAULT_PAGE_SIZE,
                Sort.by(Sort.Direction.DESC, RestaurantSorter.VOTES.getFieldName()));
        List<Restaurant> realRestaurants = restaurantService.getAllRestaurants(null, pageable);
        assertThat(realRestaurants)
                .hasSameSizeAs(testRestaurants)
                .usingRecursiveComparison()
                .isEqualTo(testRestaurants);
    }

    @Test
    public void getAllRestaurants_withNullListView_sortedById_Ascending_withFirstPageRequested() {
        List<Restaurant> testRestaurants = Collections.singletonList(testRestaurant1);
        Pageable pageable = PageRequest.of(
                0, 1,
                Sort.by(Sort.Direction.ASC, RestaurantSorter.ID.getFieldName()));
        List<Restaurant> realRestaurants = restaurantService.getAllRestaurants(null, pageable);
        assertThat(realRestaurants)
                .hasSameSizeAs(testRestaurants)
                .usingRecursiveComparison()
                .isEqualTo(testRestaurants);
    }

    @Test
    public void getAllRestaurants_withNullListView_sortedById_Ascending_withSecondPageRequested() {
        List<Restaurant> testRestaurants = Collections.singletonList(testRestaurant2);
        Pageable pageable = PageRequest.of(
                1, 1,
                Sort.by(Sort.Direction.ASC, RestaurantSorter.ID.getFieldName()));
        List<Restaurant> realRestaurants = restaurantService.getAllRestaurants(null, pageable);
        assertThat(realRestaurants)
                .hasSameSizeAs(testRestaurants)
                .usingRecursiveComparison()
                .isEqualTo(testRestaurants);
    }

    @Test
    public void getRestaurantById() {
        Restaurant realRestaurant = restaurantService.getRestaurantById(1);
        assertThat(realRestaurant)
                .usingRecursiveComparison()
                .isEqualTo(testRestaurant1);
    }

    @Test
    public void getRestaurantById_withNonExistingRestaurantId() {
        int id = -1;
        assertThatThrownBy(() -> restaurantService.getRestaurantById(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Restaurant with id=" + id + " not found.");
    }

    @Test
    public void getRestaurantById_withNullRestaurantId() {
        assertThatThrownBy(() -> restaurantService.getRestaurantById(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ENTITY_ID_MUST_NOT_BE_NULL_MESSAGE);
    }

    @Test
    public void getMealById() {
        Meal testMeal = testRestaurant1.getMeals().get(0);
        Meal realMeal = restaurantService.getMealById(1);
        assertThat(realMeal)
                .usingRecursiveComparison()
                .isEqualTo(testMeal);
    }

    @Test
    public void getMealById_withNonExistingMealId() {
        Integer id = -1;
        assertThatThrownBy(() -> restaurantService.getMealById(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Meal with id=" + id + " not found.");
    }

    @Test
    public void getMealById_withNullMealId() {
        assertThatThrownBy(() -> restaurantService.getMealById(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ENTITY_ID_MUST_NOT_BE_NULL_MESSAGE);
    }

}