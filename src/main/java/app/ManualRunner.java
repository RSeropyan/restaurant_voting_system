package app;

import app.entity.Restaurant;
import app.service.RestaurantService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.ArrayList;

public class ManualRunner {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.getEnvironment().setActiveProfiles("dev");
        ctx.scan("app.config");
        ctx.refresh();

        RestaurantService restaurantService = (RestaurantService) ctx.getBean("restaurantService");
        Restaurant restaurant = new Restaurant(1, "MacDonalds", 100);
        restaurant.setMeals(new ArrayList<>());
        restaurantService.updateById(1, restaurant);

    }

}
