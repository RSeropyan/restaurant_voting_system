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

        Restaurant restaurant = new Restaurant(1, "MacDonalds", 100);
        List<Meal> meals = new ArrayList<>();
        Meal meal = new Meal(null, "Sandwich", MealCategory.MAIN, 200, restaurant);
        meals.add(meal);
        restaurant.setMeals(meals);
        restaurantService.updateById(1, restaurant);

    }

}
