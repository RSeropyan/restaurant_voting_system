package app.service;

import app.entity.Restaurant;
import app.exceptions.EntityNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;

import static app.service.testdata.RestaurantTestData.testDataRestaurant;

public class RestaurantServiceTest extends ServiceTest {

    @Autowired
    private RestaurantService restaurantService;

    @Rule
    public ExpectedException thrownException = ExpectedException.none();

    @Test
    public void getById() {
        Restaurant realRestaurant = restaurantService.getById(1);
        Assertions.assertThat(realRestaurant).usingRecursiveComparison().isEqualTo(testDataRestaurant);
    }

    @Test
    public void getByNonExistingId() {
        int id = -1;
        thrownException.expect(EntityNotFoundException.class);
        thrownException.expectMessage("Restaurant with id=" + id + " not found.");
        restaurantService.getById(id);
    }

}