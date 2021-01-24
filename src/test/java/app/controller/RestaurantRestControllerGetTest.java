package app.controller;

import app.entity.Restaurant;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.Arrays;

import static app.testdata.TestData.testRestaurant1;
import static app.testdata.TestData.testRestaurant2;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RestaurantRestControllerGetTest extends AbstractControllerTest{

    @Test
    public void getAllRestaurants_withShortView() throws Exception {
        this.mockMvc.perform(get("/restaurants"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(header().string("Cache-Control", "no-store"))
                .andExpect(restaurantShortView("$[0]", testRestaurant2))
                .andExpect(restaurantShortView("$[1]", testRestaurant1));
    }

    @Test
    public void getAllRestaurants_withDetailedView() throws Exception {
        this.mockMvc.perform(get("/restaurants?view=detailed"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(header().string("Cache-Control", "no-store"))
                .andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(testRestaurant1, testRestaurant2))));
    }

    public static ResultMatcher restaurantShortView(String prefix, Restaurant restaurant) {
        return ResultMatcher.matchAll(
                jsonPath(prefix + ".id").value(restaurant.getId()),
                jsonPath(prefix + ".name").value(restaurant.getName()),
                jsonPath(prefix + ".votes").value(restaurant.getVotes())
        );
    }

    @Test
    public void getMealById() throws Exception {
        this.mockMvc.perform(get("/restaurants/meals/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(header().string("Cache-Control", "no-store"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Ceaser Salad"))
                .andExpect(jsonPath("$.category").value("SALAD"))
                .andExpect(jsonPath("$.price").value(350));
    }

    @Test
    public void getMealById_withNonExistingId() throws Exception {
        Integer id = -1;
        this.mockMvc.perform(get("/restaurants/meals/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(header().string("Cache-Control", "no-store"))
                .andExpect(jsonPath("$.errorClass").value("EntityNotFoundException"))
                .andExpect(jsonPath("$.errorMessage").value("Meal with id=" + id + " not found."));
    }

    @Test
    public void getMealById_withInvalidIdType() throws Exception {
        this.mockMvc.perform(get("/restaurants/meals/{id}", "xxx"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(header().string("Cache-Control", "no-store"))
                .andExpect(jsonPath("$.errorClass").value("MethodArgumentTypeMismatchException"));
    }

    @Test
    public void getRestaurantById() throws Exception {
        this.mockMvc.perform(get("/restaurants/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(header().string("Cache-Control", "no-store"))
                .andExpect(content().json(objectMapper.writeValueAsString(testRestaurant1)));
    }

    @Test
    public void getRestaurantById_withNonExistingId() throws Exception {
        Integer id = -1;
        this.mockMvc.perform(get("/restaurants/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(header().string("Cache-Control", "no-store"))
                .andExpect(jsonPath("$.errorClass").value("EntityNotFoundException"))
                .andExpect(jsonPath("$.errorMessage").value("Restaurant with id=" + id + " not found."))
                .andReturn();
    }

    @Test
    public void getRestaurantById_withInvalidIdType() throws Exception {
        this.mockMvc.perform(get("/restaurants/{id}", "xxx"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(header().string("Cache-Control", "no-store"))
                .andExpect(jsonPath("$.errorClass").value("MethodArgumentTypeMismatchException"));
    }

}