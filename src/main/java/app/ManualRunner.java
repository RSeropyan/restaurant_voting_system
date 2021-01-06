package app;

import app.entity.Meal;
import app.service.RestaurantService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class ManualRunner {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
//        ctx.getEnvironment().setActiveProfiles("dev");
        ctx.scan("app.config");
        ctx.refresh();

        RestaurantService restaurantService = (RestaurantService) ctx.getBean("restaurantService");
        List<Meal> meals = restaurantService.getAllMealsByRestaurantId(1);
        meals.forEach(meal -> System.out.println(meal.toString()));
    }

}
