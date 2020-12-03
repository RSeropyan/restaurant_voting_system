package app;

import app.entity.Meal;
import app.entity.MealCategory;
import app.entity.Restaurant;
import app.service.RestaurantService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;
import java.util.List;

public class ManualRunner {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.getEnvironment().setActiveProfiles("dev");
        ctx.scan("app.config");
        ctx.refresh();

        RestaurantService restaurantService = (RestaurantService) ctx.getBean("restaurantService");

        Restaurant restaurant = new Restaurant(2, "Marcellis", 100);
        List<Meal> meals = new ArrayList<>();
        restaurant.setMeals(meals);
        restaurantService.updateById(2, restaurant);

    }

}
