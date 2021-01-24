package app.controller;

import app.entity.Restaurant;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.Arrays;

import static app.testdata.TestData.testRestaurant1;
import static app.testdata.TestData.testRestaurant2;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RestaurantRestControllerGetTest extends AbstractControllerTest{

    public static ResultMatcher restaurantShortView(String prefix, Restaurant restaurant) {
        return ResultMatcher.matchAll(
                jsonPath(prefix + ".id").value(restaurant.getId()),
                jsonPath(prefix + ".name").value(restaurant.getName()),
                jsonPath(prefix + ".votes").value(restaurant.getVotes())
        );
    }

    @Test
    public void getAllRestaurants_withShortView_withDefaultPaginationAndSorting() throws Exception {
        this.mockMvc.perform(get("/restaurants"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(header().string("Cache-Control", "no-store"))
                .andExpect(restaurantShortView("$[0]", testRestaurant2))
                .andExpect(restaurantShortView("$[1]", testRestaurant1));
    }

    @Test
    public void getAllRestaurants_withDetailedView_withSortedByVotesDescending() throws Exception {
        // Sorting restaurants by votes in descending order is default option in both controller and service layers
        MvcResult mvcResult = this.mockMvc.perform(
                get("/restaurants")
                        .param("view", "detailed"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(header().string("Cache-Control", "no-store"))
                .andReturn();

        String actual = mvcResult.getResponse().getContentAsString();
        String expected = objectMapper.writeValueAsString(Arrays.asList(testRestaurant2, testRestaurant1));
        JSONAssert.assertEquals(expected, actual, JSONCompareMode.STRICT);
    }

    @Test
    public void getAllRestaurants_withDetailedView_withSortedByVotesAscending() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
                get("/restaurants")
                        .param("view", "detailed")
                        .param("sort", "votes")
                        .param("sdir", "asc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(header().string("Cache-Control", "no-store"))
                .andReturn();

        String actual = mvcResult.getResponse().getContentAsString();
        String expected = objectMapper.writeValueAsString(Arrays.asList(testRestaurant1, testRestaurant2));
        JSONAssert.assertEquals(expected, actual, JSONCompareMode.STRICT);
    }

    @Test
    public void getAllRestaurants_withDetailedView_withSortedByIdDescending() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
                get("/restaurants")
                        .param("view", "detailed")
                        .param("sort", "id")
                        .param("sdir", "desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(header().string("Cache-Control", "no-store"))
                .andReturn();

        String actual = mvcResult.getResponse().getContentAsString();
        String expected = objectMapper.writeValueAsString(Arrays.asList(testRestaurant2, testRestaurant1));
        JSONAssert.assertEquals(expected, actual, JSONCompareMode.STRICT);
    }

    @Test
    public void getAllRestaurants_withDetailedView_withSortedByIdAscending() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
                get("/restaurants")
                        .param("view", "detailed")
                        .param("sort", "id")
                        .param("sdir", "asc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(header().string("Cache-Control", "no-store"))
                .andReturn();

        String actual = mvcResult.getResponse().getContentAsString();
        String expected = objectMapper.writeValueAsString(Arrays.asList(testRestaurant1, testRestaurant2));
        JSONAssert.assertEquals(expected, actual, JSONCompareMode.STRICT);
    }

    @Test
    public void getAllRestaurants_withDetailedView_withSortedByNameDescending() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
                get("/restaurants")
                        .param("view", "detailed")
                        .param("sort", "name")
                        .param("sdir", "desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(header().string("Cache-Control", "no-store"))
                .andReturn();

        String actual = mvcResult.getResponse().getContentAsString();
        String expected = objectMapper.writeValueAsString(Arrays.asList(testRestaurant2, testRestaurant1));
        JSONAssert.assertEquals(expected, actual, JSONCompareMode.STRICT);
    }

    @Test
    public void getAllRestaurants_withDetailedView_withSortedByNameAscending() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(
                get("/restaurants")
                        .param("view", "detailed")
                        .param("sort", "name")
                        .param("sdir", "asc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(header().string("Cache-Control", "no-store"))
                .andReturn();

        String actual = mvcResult.getResponse().getContentAsString();
        String expected = objectMapper.writeValueAsString(Arrays.asList(testRestaurant1, testRestaurant2));
        JSONAssert.assertEquals(expected, actual, JSONCompareMode.STRICT);
    }

    @Test
    public void getAllRestaurants_withFirstPageExpected() throws Exception{
        this.mockMvc.perform(
                get("/restaurants")
                        .param("view", "detailed")
                        .param("currentPage", "0")
                        .param("pageSize", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(header().string("Cache-Control", "no-store"))
                .andExpect(restaurantShortView("$[0]", testRestaurant2));
    }

    @Test
    public void getAllRestaurants_withSecondPageExpected() throws Exception {
        this.mockMvc.perform(
                get("/restaurants")
                        .param("view", "detailed")
                        .param("currentPage", "1")
                        .param("pageSize", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(header().string("Cache-Control", "no-store"))
                .andExpect(restaurantShortView("$[0]", testRestaurant1));
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