package app;

import app.service.RestaurantService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ManualRunner {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.getEnvironment().setActiveProfiles("dev");
        ctx.scan("app.config");
        ctx.refresh();

        RestaurantService restaurantService = (RestaurantService) ctx.getBean("restaurantService");
        restaurantService.getById(1);
        restaurantService.getById(1);
    }

}
