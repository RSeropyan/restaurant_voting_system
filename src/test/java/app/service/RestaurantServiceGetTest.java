package app.service;

import app.entity.Meal;
import app.entity.Restaurant;
import app.service.exceptions.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static app.service.validation.ValidationUtil.*;
import static app.testdata.TestData.testRestaurant1;
import static app.testdata.TestData.testRestaurant2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static app.service.RestaurantService.ListView;


public class RestaurantServiceGetTest extends AbstractServiceTest{

    @Autowired
    private RestaurantService restaurantService;

//    @Test
//    public void getAllRestaurants_withShortListView_withNoPagination() {
//        List<Restaurant> testRestaurants = Arrays.asList(testRestaurant2, testRestaurant1);
//        List<Restaurant> realRestaurants = restaurantService.getAllRestaurants(ListView.SHORT);
//        assertThat(realRestaurants)
//                .hasSameSizeAs(testRestaurants)
//                .usingRecursiveComparison()
//                .isEqualTo(testRestaurants);
//    }
//
//    @Test
//    public void getAllRestaurants_withShortListView_withNullPagination() {
//        List<Restaurant> testRestaurants = Arrays.asList(testRestaurant2, testRestaurant1);
//        List<Restaurant> realRestaurants = restaurantService.getAllRestaurants(null);
//        assertThat(realRestaurants)
//                .hasSameSizeAs(testRestaurants)
//                .usingRecursiveComparison()
//                .isEqualTo(testRestaurants);
//    }

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











//    @Test
//    public void getAllRestaurants_withNullPageableInstance() {
//        List<Restaurant> testRestaurants = Arrays.asList(testRestaurant2, testRestaurant1);
//        List<Restaurant> realRestaurants = restaurantService.getAllRestaurants(null);
//        assertThat(realRestaurants)
//                .hasSameSizeAs(testRestaurants)
//                .usingRecursiveComparison()
//                .isEqualTo(testRestaurants);
//    }

//    @Test
//    public void getAllRestaurants_withSortedById() {
//        List<Restaurant> testRestaurants = Arrays.asList(testRestaurant2, testRestaurant1);
//        Pageable pageable = PageRequest.of(
//                DEFAULT_CURRENT_PAGE,
//                DEFAULT_PAGE_SIZE,
//                Sort.by(DEFAULT_SORT_DIRECTION, RestaurantSorter.ID.getFieldName()));
//        List<Restaurant> realRestaurants = restaurantService.getAllRestaurants(pageable);
//        assertThat(realRestaurants)
//                .hasSameSizeAs(testRestaurants)
//                .usingRecursiveComparison()
//                .isEqualTo(testRestaurants);
//    }

//    @Test
//    public void getAllRestaurants_withSortedByVotesAscending() {
//        List<Restaurant> testRestaurants = Arrays.asList(testRestaurant1, testRestaurant2);
//        Pageable pageable = PageRequest.of(
//                DEFAULT_CURRENT_PAGE,
//                DEFAULT_PAGE_SIZE,
//                Sort.by(Sort.Direction.ASC, RestaurantSorter.VOTES.getFieldName()));
//        List<Restaurant> realRestaurants = restaurantService.getAllRestaurants(pageable);
//        assertThat(realRestaurants)
//                .hasSameSizeAs(testRestaurants)
//                .usingRecursiveComparison()
//                .isEqualTo(testRestaurants);
//    }

//    @Test
//    public void getAllRestaurants_withSortedByVotesDescending() {
//        List<Restaurant> testRestaurants = Arrays.asList(testRestaurant2, testRestaurant1);
//        Pageable pageable = PageRequest.of(
//                DEFAULT_CURRENT_PAGE,
//                DEFAULT_PAGE_SIZE,
//                Sort.by(Sort.Direction.DESC, RestaurantSorter.VOTES.getFieldName()));
//        List<Restaurant> realRestaurants = restaurantService.getAllRestaurants(pageable);
//        assertThat(realRestaurants)
//                .hasSameSizeAs(testRestaurants)
//                .usingRecursiveComparison()
//                .isEqualTo(testRestaurants);
//    }

//    @Test
//    public void getAllRestaurants_withSortedByNameAscending() {
//        List<Restaurant> testRestaurants = Arrays.asList(testRestaurant1, testRestaurant2);
//        Pageable pageable = PageRequest.of(
//                DEFAULT_CURRENT_PAGE,
//                DEFAULT_PAGE_SIZE,
//                Sort.by(Sort.Direction.ASC, RestaurantSorter.NAME.getFieldName()));
//        List<Restaurant> realRestaurants = restaurantService.getAllRestaurants(pageable);
//        assertThat(realRestaurants)
//                .hasSameSizeAs(testRestaurants)
//                .usingRecursiveComparison()
//                .isEqualTo(testRestaurants);
//    }

//    @Test
//    public void getAllRestaurants_withSortedByNameDescending() {
//        List<Restaurant> testRestaurants = Arrays.asList(testRestaurant2, testRestaurant1);
//        Pageable pageable = PageRequest.of(
//                DEFAULT_CURRENT_PAGE,
//                DEFAULT_PAGE_SIZE,
//                Sort.by(Sort.Direction.DESC, RestaurantSorter.NAME.getFieldName()));
//        List<Restaurant> realRestaurants = restaurantService.getAllRestaurants(pageable);
//        assertThat(realRestaurants)
//                .hasSameSizeAs(testRestaurants)
//                .usingRecursiveComparison()
//                .isEqualTo(testRestaurants);
//    }

//    @Test
//    public void getAllRestaurants_withFirstPageExpected() {
//        List<Restaurant> testRestaurants = Arrays.asList(testRestaurant2);
//        Pageable pageable = PageRequest.of(
//                DEFAULT_CURRENT_PAGE,
//                1,
//                Sort.by(DEFAULT_SORT_DIRECTION, DEFAULT_SORTED_BY.getFieldName()));
//        List<Restaurant> realRestaurants = restaurantService.getAllRestaurants(pageable);
//        assertThat(realRestaurants)
//                .hasSameSizeAs(testRestaurants)
//                .usingRecursiveComparison()
//                .isEqualTo(testRestaurants);
//    }

//    @Test
//    public void getAllRestaurants_withSecondPageExpected() {
//        List<Restaurant> testRestaurants = Arrays.asList(testRestaurant1);
//        Pageable pageable = PageRequest.of(
//                1,
//                1,
//                Sort.by(DEFAULT_SORT_DIRECTION, DEFAULT_SORTED_BY.getFieldName()));
//        List<Restaurant> realRestaurants = restaurantService.getAllRestaurants(pageable);
//        assertThat(realRestaurants)
//                .hasSameSizeAs(testRestaurants)
//                .usingRecursiveComparison()
//                .isEqualTo(testRestaurants);
//    }
//
//    @Test
//    public void getRestaurantById() {
//        Restaurant realRestaurant1 = restaurantService.getRestaurantById(1);
//        assertThat(realRestaurant1)
//                .usingRecursiveComparison()
//                .isEqualTo(testRestaurant1);
//    }
//
//    @Test
//    public void getRestaurantById_withNonExistingRestaurantId() {
//        int id = -1;
//        assertThatThrownBy(() -> restaurantService.getRestaurantById(id))
//                .isInstanceOf(EntityNotFoundException.class)
//                .hasMessage("Restaurant with id=" + id + " not found.");
//    }
//
//    @Test
//    public void getRestaurantById_withNullRestaurantId() {
//        assertThatThrownBy(() -> restaurantService.getRestaurantById(null))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessageContaining(MESSAGE_checkNotNullId);
//    }
//
//    @Test
//    public void getMealById() {
//        Meal realMeal = restaurantService.getMealById(1);
//        assertThat(realMeal)
//                .usingRecursiveComparison()
//                .ignoringFields("restaurant")
//                .isEqualTo(testRestaurant1.getMeals().get(0));
//        assertThat(realMeal.getRestaurant().getId()).isEqualTo(1);
//    }
//
//    @Test
//    public void getMealById_withNonExistingMealId() {
//        Integer id = -1;
//        assertThatThrownBy(() -> restaurantService.getMealById(id))
//                .isInstanceOf(EntityNotFoundException.class)
//                .hasMessageContaining("Meal with id=" + id + " not found.");
//    }
//
//    @Test
//    public void getMealById_withNullMealId() {
//        assertThatThrownBy(() -> restaurantService.getMealById(null))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessage(MESSAGE_checkNotNullId);
//    }

}