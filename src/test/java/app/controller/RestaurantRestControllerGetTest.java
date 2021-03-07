package app.controller;

import app.entity.Meal;
import app.entity.Restaurant;
import app.service.RestaurantService;
import app.service.exceptions.EntityNotFoundException;
import app.service.helpers.RestaurantSorter;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static app.service.RestaurantService.DEFAULT_CURRENT_PAGE;
import static app.service.RestaurantService.DEFAULT_PAGE_SIZE;
import static app.service.validation.ValidationUtil.ENTITY_ID_MUST_NOT_BE_NULL_MESSAGE;
import static app.testdata.TestData.testRestaurant1;
import static app.testdata.TestData.testRestaurant2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RestaurantRestControllerGetTest extends AbstractControllerTest{

    // REST API Endpoints to test:
    //    /restaurants                            	  - get all restaurants
    //    /restaurants/{id}                     	  - get restaurant by id
    //    /restaurants/meals/{id}               	  - get meal by id


//    @Test
//    public void getAllRestaurants_withNullListView_withNullPagination() throws Exception {
//
//    }
//
//    @Test
//    public void getAllRestaurants_withShortListView_withNullPagination() {
//
//    }
//
//    @Test
//    public void getAllRestaurants_withDetailedListView_withNullPagination() {
//
//    }
//
//    @Test
//    public void getAllRestaurants_withNullListView_sortedById_Ascending() {
//
//    }
//
//    @Test
//    public void getAllRestaurants_withNullListView_sortedById_Descending() {
//
//    }
//
//    @Test
//    public void getAllRestaurants_withNullListView_sortedByName_Ascending() {
//
//    }
//
//    @Test
//    public void getAllRestaurants_withNullListView_sortedByName_Descending() {
//
//    }
//
//    @Test
//    public void getAllRestaurants_withNullListView_sortedByVotes_Ascending() {
//
//    }
//
//    @Test
//    public void getAllRestaurants_withNullListView_sortedByVotes_Descending() {
//
//    }
//
//    @Test
//    public void getAllRestaurants_withNullListView_sortedById_Ascending_withFirstPageRequested() {
//
//    }
//
//    @Test
//    public void getAllRestaurants_withNullListView_sortedById_Ascending_withSecondPageRequested() {
//
//    }

    @Test
    public void getRestaurantById() throws Exception {
        this.mockMvc.perform(get("/restaurants/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(header().string("Cache-Control", "no-store"))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().json(objectMapper.writeValueAsString(testRestaurant1)));
    }

    @Test
    public void getRestaurantById_withNonExistingRestaurantId() throws Exception {
        Integer id = -1;
        this.mockMvc.perform(get("/restaurants/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(header().string("Cache-Control", "no-store"))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errors[0]").value("Restaurant with id=" + id + " not found."));
    }

    @Test
    public void getRestaurantById_withInvalidIdType() throws Exception {
        this.mockMvc.perform(get("/restaurants/{id}", "xyz"))
                .andExpect(status().isBadRequest())
                .andExpect(header().string("Cache-Control", "no-store"))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errors[0]").value("Entity id must be a positive number."));
    }

    @Test
    public void getMealById() throws Exception {
        this.mockMvc.perform(get("/restaurants/meals/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(header().string("Cache-Control", "no-store"))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(content().json(objectMapper.writeValueAsString(testRestaurant1.getMeals().get(0))));
    }

    @Test
    public void getMealById_withNonExistingMealId() throws Exception {
        Integer id = -1;
        this.mockMvc.perform(get("/restaurants/meals/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(header().string("Cache-Control", "no-store"))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errors[0]").value("Meal with id=" + id + " not found."));
    }

    @Test
    public void getMealById_withInvalidIdType() throws Exception {
        this.mockMvc.perform(get("/restaurants/meals/{id}", "xyz"))
                .andExpect(status().isBadRequest())
                .andExpect(header().string("Cache-Control", "no-store"))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.errors[0]").value("Entity id must be a positive number."));
    }

//    public static ResultMatcher restaurantShortView(String prefix, Restaurant restaurant) {
//        return ResultMatcher.matchAll(
//                jsonPath(prefix + ".id").value(restaurant.getId()),
//                jsonPath(prefix + ".name").value(restaurant.getName()),
//                jsonPath(prefix + ".votes").value(restaurant.getVotes())
//        );
//    }
//
//    @Test
//    public void getAllRestaurants_withShortView_withDefaultPaginationAndSorting() throws Exception {
//        this.mockMvc.perform(get("/restaurants"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andExpect(header().string("Cache-Control", "no-store"))
//                .andExpect(restaurantShortView("$[0]", testRestaurant2))
//                .andExpect(restaurantShortView("$[1]", testRestaurant1));
//    }

//    @Test
//    public void getAllRestaurants_withDetailedView_withSortedByVotesDescending() throws Exception {
//        // Sorting restaurants by votes in descending order is default option in both controller and service layers
//        MvcResult mvcResult = this.mockMvc.perform(
//                get("/restaurants")
//                        .param("view", "detailed"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andExpect(header().string("Cache-Control", "no-store"))
//                .andReturn();
//
//        String actual = mvcResult.getResponse().getContentAsString();
//        String expected = objectMapper.writeValueAsString(Arrays.asList(testRestaurant2, testRestaurant1));
//        JSONAssert.assertEquals(expected, actual, JSONCompareMode.STRICT);
//    }
//
//    @Test
//    public void getAllRestaurants_withDetailedView_withSortedByVotesAscending() throws Exception {
//        MvcResult mvcResult = this.mockMvc.perform(
//                get("/restaurants")
//                        .param("view", "detailed")
//                        .param("sort", "votes")
//                        .param("sdir", "asc"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andExpect(header().string("Cache-Control", "no-store"))
//                .andReturn();
//
//        String actual = mvcResult.getResponse().getContentAsString();
//        String expected = objectMapper.writeValueAsString(Arrays.asList(testRestaurant1, testRestaurant2));
//        JSONAssert.assertEquals(expected, actual, JSONCompareMode.STRICT);
//    }
//
//    @Test
//    public void getAllRestaurants_withDetailedView_withSortedByIdDescending() throws Exception {
//        MvcResult mvcResult = this.mockMvc.perform(
//                get("/restaurants")
//                        .param("view", "detailed")
//                        .param("sort", "id")
//                        .param("sdir", "desc"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andExpect(header().string("Cache-Control", "no-store"))
//                .andReturn();
//
//        String actual = mvcResult.getResponse().getContentAsString();
//        String expected = objectMapper.writeValueAsString(Arrays.asList(testRestaurant2, testRestaurant1));
//        JSONAssert.assertEquals(expected, actual, JSONCompareMode.STRICT);
//    }
//
//    @Test
//    public void getAllRestaurants_withDetailedView_withSortedByIdAscending() throws Exception {
//        MvcResult mvcResult = this.mockMvc.perform(
//                get("/restaurants")
//                        .param("view", "detailed")
//                        .param("sort", "id")
//                        .param("sdir", "asc"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andExpect(header().string("Cache-Control", "no-store"))
//                .andReturn();
//
//        String actual = mvcResult.getResponse().getContentAsString();
//        String expected = objectMapper.writeValueAsString(Arrays.asList(testRestaurant1, testRestaurant2));
//        JSONAssert.assertEquals(expected, actual, JSONCompareMode.STRICT);
//    }
//
//    @Test
//    public void getAllRestaurants_withDetailedView_withSortedByNameDescending() throws Exception {
//        MvcResult mvcResult = this.mockMvc.perform(
//                get("/restaurants")
//                        .param("view", "detailed")
//                        .param("sort", "name")
//                        .param("sdir", "desc"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andExpect(header().string("Cache-Control", "no-store"))
//                .andReturn();
//
//        String actual = mvcResult.getResponse().getContentAsString();
//        String expected = objectMapper.writeValueAsString(Arrays.asList(testRestaurant2, testRestaurant1));
//        JSONAssert.assertEquals(expected, actual, JSONCompareMode.STRICT);
//    }
//
//    @Test
//    public void getAllRestaurants_withDetailedView_withSortedByNameAscending() throws Exception {
//        MvcResult mvcResult = this.mockMvc.perform(
//                get("/restaurants")
//                        .param("view", "detailed")
//                        .param("sort", "name")
//                        .param("sdir", "asc"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andExpect(header().string("Cache-Control", "no-store"))
//                .andReturn();
//
//        String actual = mvcResult.getResponse().getContentAsString();
//        String expected = objectMapper.writeValueAsString(Arrays.asList(testRestaurant1, testRestaurant2));
//        JSONAssert.assertEquals(expected, actual, JSONCompareMode.STRICT);
//    }
//
//    @Test
//    public void getAllRestaurants_withFirstPageExpected() throws Exception{
//        this.mockMvc.perform(
//                get("/restaurants")
//                        .param("view", "detailed")
//                        .param("currentPage", "0")
//                        .param("pageSize", "1"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andExpect(header().string("Cache-Control", "no-store"))
//                .andExpect(restaurantShortView("$[0]", testRestaurant2));
//    }
//
//    @Test
//    public void getAllRestaurants_withSecondPageExpected() throws Exception {
//        this.mockMvc.perform(
//                get("/restaurants")
//                        .param("view", "detailed")
//                        .param("currentPage", "1")
//                        .param("pageSize", "1"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=UTF-8"))
//                .andExpect(header().string("Cache-Control", "no-store"))
//                .andExpect(restaurantShortView("$[0]", testRestaurant1));
//    }




}