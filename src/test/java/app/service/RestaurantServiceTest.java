package app.service;

import app.entity.Restaurant;
import app.exceptions.EntityNotFoundException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static app.service.testdata.RestaurantTestData.testRestaurant1;
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

}