package app.service;

import app.entity.Meal;
import app.entity.Restaurant;
import app.exceptions.EntityNotFoundException;
import app.service.utils.RestaurantSorter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static app.service.testdata.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

//@ActiveProfiles("dev")
@SpringJUnitConfig(app.config.DbConfig.class)
@Sql(scripts = "/mysql_script.sql")
@Transactional
public class RestaurantServiceTest {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private RestaurantVotingService restaurantVotingService;

    @BeforeEach
    public void refreshTestData() {
        initializeTestData();
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
    public void getAllRestaurants_withNotPageableNotSorted() {
        List<Restaurant> testRestaurants = Arrays.asList(testRestaurant2, testRestaurant1);
        List<Restaurant> realRestaurants = restaurantService.getAllRestaurants(null, null, null, null);
        assertThat(realRestaurants).hasSameSizeAs(testRestaurants)
                .usingRecursiveComparison().isEqualTo(testRestaurants);
    }

    @Test
    public void getAllRestaurants_withSortedById() {
        List<Restaurant> testRestaurants = Arrays.asList(testRestaurant2, testRestaurant1);
        List<Restaurant> realRestaurants = restaurantService.getAllRestaurants(null, null, RestaurantSorter.ID, null);
        assertThat(realRestaurants).hasSameSizeAs(testRestaurants)
                .usingRecursiveComparison().isEqualTo(testRestaurants);
    }

    @Test
    public void getAllRestaurants_withSortedByVotesAscending() {
        List<Restaurant> testRestaurants = Arrays.asList(testRestaurant1, testRestaurant2);
        List<Restaurant> realRestaurants = restaurantService.getAllRestaurants(null, null, RestaurantSorter.VOTES, Sort.Direction.ASC);
        assertThat(realRestaurants).hasSameSizeAs(testRestaurants)
                .usingRecursiveComparison().isEqualTo(testRestaurants);
    }

    @Test
    public void getAllRestaurants_withSortedByVotesDescending() {
        List<Restaurant> testRestaurants = Arrays.asList(testRestaurant2, testRestaurant1);
        List<Restaurant> realRestaurants = restaurantService.getAllRestaurants(null, null, RestaurantSorter.VOTES, Sort.Direction.DESC);
        assertThat(realRestaurants).hasSameSizeAs(testRestaurants)
                .usingRecursiveComparison().isEqualTo(testRestaurants);
    }

    @Test
    public void getAllRestaurants_withSortedByNameAscending() {
        List<Restaurant> testRestaurants = Arrays.asList(testRestaurant1, testRestaurant2);
        List<Restaurant> realRestaurants = restaurantService.getAllRestaurants(null, null, RestaurantSorter.NAME, Sort.Direction.ASC);
        assertThat(realRestaurants).hasSameSizeAs(testRestaurants)
                .usingRecursiveComparison().isEqualTo(testRestaurants);
    }

    @Test
    public void getAllRestaurants_withSortedByNameDescending() {
        List<Restaurant> testRestaurants = Arrays.asList(testRestaurant2, testRestaurant1);
        List<Restaurant> realRestaurants = restaurantService.getAllRestaurants(null, null, RestaurantSorter.NAME, Sort.Direction.DESC);
        assertThat(realRestaurants).hasSameSizeAs(testRestaurants)
                .usingRecursiveComparison().isEqualTo(testRestaurants);
    }

    @Test
    public void getAllRestaurants_withExpectedFirstPage() {
        List<Restaurant> testRestaurants = Arrays.asList(testRestaurant2);
        List<Restaurant> realRestaurants = restaurantService.getAllRestaurants(0, 1, null, null);
        assertThat(realRestaurants).hasSameSizeAs(testRestaurants)
                .usingRecursiveComparison().isEqualTo(testRestaurants);
    }

    @Test
    public void getAllRestaurants_withExpectedSecondPage() {
        List<Restaurant> testRestaurants = Arrays.asList(testRestaurant1);
        List<Restaurant> realRestaurants = restaurantService.getAllRestaurants(1, 1, null, null);
        assertThat(realRestaurants).hasSameSizeAs(testRestaurants)
                .usingRecursiveComparison().isEqualTo(testRestaurants);
    }

    @Test
    public void deleteRestaurantById() {
        List<Restaurant> testRestaurants = Arrays.asList(testRestaurant2);
        restaurantService.deleteRestaurantById(1);
        List<Restaurant> realRestaurants = restaurantService.getAllRestaurants(null, null, null, null);
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
    public void deleteAllRestaurants() {
        restaurantService.deleteAllRestaurants();
        assertThat(restaurantService.getAllRestaurants(null, null, null, null).size()).isEqualTo(0);
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

    @Test
    public void createRestaurant() {
        Integer realRestaurantId = restaurantService.createRestaurant(testRestaurant3);
        testRestaurant3.setId(3);
        assertThat(realRestaurantId).isEqualTo(testRestaurant3.getId());
    }

    @Test
    public void createRestaurant_withExistingName() {
        testRestaurant3.setName(testRestaurant1.getName());
        assertThatThrownBy(() -> restaurantService.createRestaurant(testRestaurant3))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    public void createRestaurant_withNullEntity() {
        assertThatThrownBy(() -> restaurantService.createRestaurant(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The entity must not be null.");
    }

    @Test
    public void createRestaurant_withNotNullEntityId() {
        testRestaurant3.setId(0);
        assertThatThrownBy(() -> restaurantService.createRestaurant(testRestaurant3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The entity id must be null.");
    }

    @Test
    public void createRestaurant_withNullProperties() {
        testRestaurant3.setName(null);
        testRestaurant3.setVotes(null);
        testRestaurant3.setMeals(null);
        assertThatThrownBy(() -> restaurantService.createRestaurant(testRestaurant3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The entity's properties must not be null (except id and restaurant).");
    }

    @Test
    public void updateRestaurantById() {
        restaurantService.updateRestaurantById(2, testRestaurant3);
        Restaurant realRestaurant = restaurantService.getRestaurantById(2);
        testRestaurant3.setId(realRestaurant.getId());
        assertThat(realRestaurant).usingRecursiveComparison().isEqualTo(testRestaurant3);
    }

    @Test
    public void updateRestaurantById_withNonExistingId() {
        Integer id = -1;
        assertThatThrownBy(() -> restaurantService.updateRestaurantById(id, testRestaurant1))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Restaurant with id=" + id + " not found.");
    }

    @Test
    public void updateRestaurantById_withExistingName() {
        testRestaurant3.setName(testRestaurant1.getName());
        assertThatThrownBy( () -> restaurantService.updateRestaurantById(2, testRestaurant3))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    public void updateRestaurantById_withNullEntity() {
        assertThatThrownBy(() -> restaurantService.updateRestaurantById(1, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The entity must not be null.");
    }

    @Test
    public void updateRestaurantById_withNullEntityId() {
        assertThatThrownBy(() -> { restaurantService.updateRestaurantById(null, testRestaurant3); })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The entity id must not be null.");
    }

    @Test
    public void updateRestaurantById_withNullProperties() {
        testRestaurant3.setName(null);
        testRestaurant3.setVotes(null);
        testRestaurant3.setMeals(null);
        assertThatThrownBy(() -> { restaurantService.updateRestaurantById(1, testRestaurant3); })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The entity's properties must not be null (except id and restaurant).");
    }

    @Test
    public void voteForRestaurantById() {
        Restaurant restaurant = restaurantService.getRestaurantById(1);
        Integer initialNumberOfVotes = restaurant.getVotes();
        restaurantVotingService.voteForRestaurantById(1);
        restaurant = restaurantService.getRestaurantById(1);
        Integer incrementedNumberOfVotes = restaurant.getVotes();
        assertThat(++initialNumberOfVotes).isEqualTo(incrementedNumberOfVotes);
    }

    @Test
    public void voteForRestaurantById_withNullId() {
        assertThatThrownBy(() -> restaurantVotingService.voteForRestaurantById(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The entity id must not be null.");
    }

    @Test
    public void voteForRestaurantById_withNonExistingId() {
        Integer id = -1;
        assertThatThrownBy(() -> restaurantVotingService.voteForRestaurantById(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Restaurant with id=" + id + " not found.");
    }

}