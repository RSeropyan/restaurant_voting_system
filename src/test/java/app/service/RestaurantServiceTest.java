package app.service;

import app.entity.Restaurant;
import app.exceptions.EntityNotFoundException;
import app.service.utils.RestaurantSorter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;

import static app.service.testdata.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class RestaurantServiceTest extends AbstractServiceTest {

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private VotingService votingService;

    @BeforeEach
    public void refreshTestData() {
        initializeTestData();
    }

    @Test
    public void getById() {
        Restaurant realRestaurant1 = restaurantService.getById(1);
        assertThat(realRestaurant1).usingRecursiveComparison().isEqualTo(testRestaurant1);
    }

    @Test
    public void getById_withNonExistingId() {
        int id = -1;
        assertThatThrownBy(() -> restaurantService.getById(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Restaurant with id=" + id + " not found.");
    }

    @Test
    public void getById_withNullId() {
        assertThatThrownBy(() -> restaurantService.getById(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The entity id must not be null.");
    }

    @Test
    public void getTotalNumber() {
        List<Restaurant> testRestaurants = Arrays.asList(testRestaurant1, testRestaurant2);
        Integer realTotalNumber = restaurantService.getTotalNumber();
        assertThat(realTotalNumber).isEqualTo(testRestaurants.size());
    }

    @Test
    public void getAll_withNotPageableNotSorted() {
        List<Restaurant> testRestaurants = Arrays.asList(testRestaurant2, testRestaurant1);
        List<Restaurant> realRestaurants = restaurantService.getAll(null, null, null, null);
        assertThat(realRestaurants).hasSameSizeAs(testRestaurants)
                .usingRecursiveComparison().isEqualTo(testRestaurants);
    }

    @Test
    public void getAll_withSortedById() {
        List<Restaurant> testRestaurants = Arrays.asList(testRestaurant2, testRestaurant1);
        List<Restaurant> realRestaurants = restaurantService.getAll(null, null, RestaurantSorter.byID, null);
        assertThat(realRestaurants).hasSameSizeAs(testRestaurants)
                .usingRecursiveComparison().isEqualTo(testRestaurants);
    }

    @Test
    public void getAll_withSortedByVotesAscending() {
        List<Restaurant> testRestaurants = Arrays.asList(testRestaurant1, testRestaurant2);
        List<Restaurant> realRestaurants = restaurantService.getAll(null, null, RestaurantSorter.byVOTES, Sort.Direction.ASC);
        assertThat(realRestaurants).hasSameSizeAs(testRestaurants)
                .usingRecursiveComparison().isEqualTo(testRestaurants);
    }

    @Test
    public void getAll_withSortedByVotesDescending() {
        List<Restaurant> testRestaurants = Arrays.asList(testRestaurant2, testRestaurant1);
        List<Restaurant> realRestaurants = restaurantService.getAll(null, null, RestaurantSorter.byVOTES, Sort.Direction.DESC);
        assertThat(realRestaurants).hasSameSizeAs(testRestaurants)
                .usingRecursiveComparison().isEqualTo(testRestaurants);
    }

    @Test
    public void getAll_withSortedByNameAscending() {
        List<Restaurant> testRestaurants = Arrays.asList(testRestaurant1, testRestaurant2);
        List<Restaurant> realRestaurants = restaurantService.getAll(null, null, RestaurantSorter.byNAME, Sort.Direction.ASC);
        assertThat(realRestaurants).hasSameSizeAs(testRestaurants)
                .usingRecursiveComparison().isEqualTo(testRestaurants);
    }

    @Test
    public void getAll_withSortedByNameDescending() {
        List<Restaurant> testRestaurants = Arrays.asList(testRestaurant2, testRestaurant1);
        List<Restaurant> realRestaurants = restaurantService.getAll(null, null, RestaurantSorter.byNAME, Sort.Direction.DESC);
        assertThat(realRestaurants).hasSameSizeAs(testRestaurants)
                .usingRecursiveComparison().isEqualTo(testRestaurants);
    }

    @Test
    public void getAll_withExpectedFirstPage() {
        List<Restaurant> testRestaurants = Arrays.asList(testRestaurant2);
        List<Restaurant> realRestaurants = restaurantService.getAll(0, 1, null, null);
        assertThat(realRestaurants).hasSameSizeAs(testRestaurants)
                .usingRecursiveComparison().isEqualTo(testRestaurants);
    }

    @Test
    public void getAll_withExpectedSecondPage() {
        List<Restaurant> testRestaurants = Arrays.asList(testRestaurant1);
        List<Restaurant> realRestaurants = restaurantService.getAll(1, 1, null, null);
        assertThat(realRestaurants).hasSameSizeAs(testRestaurants)
                .usingRecursiveComparison().isEqualTo(testRestaurants);
    }

    @Test
    public void deleteById() {
        List<Restaurant> testRestaurants = Arrays.asList(testRestaurant2);
        restaurantService.deleteById(1);
        List<Restaurant> realRestaurants = restaurantService.getAll(null, null, null, null);
        assertThat(realRestaurants)
                .hasSameSizeAs(testRestaurants)
                .usingRecursiveComparison().isEqualTo(testRestaurants);
    }

    @Test
    public void deleteById_withNonExistingId() {
        Integer id = -1;
        assertThatThrownBy(() -> restaurantService.deleteById(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Restaurant with id=" + id + " not found.");
    }

    @Test
    public void deleteById_withNullId() {
        assertThatThrownBy(() -> restaurantService.deleteById(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The entity id must not be null.");
    }

    @Test
    public void create() {
        Restaurant realRestaurant = restaurantService.create(testRestaurant3);
        assertThat(realRestaurant).usingRecursiveComparison().isEqualTo(testRestaurant3);
    }

    @Test
    public void create_withExistingName() {
        testRestaurant3.setName(testRestaurant1.getName());
        assertThatThrownBy(() -> restaurantService.create(testRestaurant3))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    public void create_withNullEntity() {
        assertThatThrownBy(() -> restaurantService.create(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The entity must not be null.");
    }

    @Test
    public void create_withNotNullEntityId() {
        testRestaurant3.setId(0);
        assertThatThrownBy(() -> restaurantService.create(testRestaurant3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The entity id must be null.");
    }

    @Test
    public void create_withNullProperties() {
        testRestaurant3.setName(null);
        testRestaurant3.setVotes(null);
        testRestaurant3.setMeals(null);
        assertThatThrownBy(() -> restaurantService.create(testRestaurant3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The entity's properties must not be null (except id).");
    }

    @Test
    public void updateById() {
        restaurantService.updateById(2, testRestaurant3);
        Restaurant realRestaurant = restaurantService.getById(2);
        testRestaurant3.setId(realRestaurant.getId());
        assertThat(realRestaurant).usingRecursiveComparison().isEqualTo(testRestaurant3);
    }

    @Test
    public void updateById_withNonExistingId() {
        Integer id = -1;
        assertThatThrownBy(() -> restaurantService.updateById(id, new Restaurant()))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Restaurant with id=" + id + " not found.");
    }

    @Test
    public void updateById_withExistingName() {
        testRestaurant3.setName(testRestaurant1.getName());
        assertThatThrownBy( () -> restaurantService.updateById(2, testRestaurant3))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    public void updateById_withNullEntity() {
        assertThatThrownBy(() -> restaurantService.updateById(1, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The entity must not be null.");
    }

    @Test
    public void updateById_withNullEntityId() {
        assertThatThrownBy(() -> { restaurantService.updateById(null, testRestaurant3); })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The entity id must not be null.");
    }

    @Test
    public void updateById_withNullProperties() {
        testRestaurant3.setName(null);
        testRestaurant3.setVotes(null);
        testRestaurant3.setMeals(null);
        assertThatThrownBy(() -> { restaurantService.updateById(1, testRestaurant3); })
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The entity's properties must not be null (except id).");
    }

    @Test
    public void voteById() {
        Restaurant restaurant = restaurantService.getById(1);
        Integer initialNumberOfVotes = restaurant.getVotes();
        votingService.voteById(1);
        restaurant = restaurantService.getById(1);
        Integer incrementedNumberOfVotes = restaurant.getVotes();
        assertThat(++initialNumberOfVotes).isEqualTo(incrementedNumberOfVotes);
    }

    @Test
    public void voteById_withNullId() {
        assertThatThrownBy(() -> votingService.voteById(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The entity id must not be null.");
    }

    @Test
    public void voteById_withNonExistingId() {
        Integer id = -1;
        assertThatThrownBy(() -> votingService.voteById(id))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Restaurant with id=" + id + " not found.");
    }

}