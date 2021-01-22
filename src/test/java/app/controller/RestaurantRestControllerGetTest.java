package app.controller;

import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RestaurantRestControllerGetTest extends AbstractControllerTest{

    @Test
    public void getAllRestaurants() throws Exception {
        this.mockMvc.perform(get("/restaurants"))
                .andDo(print())
                .andExpect(status().isOk());
    }

}