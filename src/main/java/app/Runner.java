package app;

import app.service.RestaurantService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Runner {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext("app.config");
        RestaurantService restaurantService = (RestaurantService) ctx.getBean("restaurantService");
        restaurantService.getAll();
        restaurantService.getAll();
    }

}
