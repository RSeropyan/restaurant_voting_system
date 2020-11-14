package app;

import app.service.RestaurantService;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Runner {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.getEnvironment().setActiveProfiles("dev");
        ctx.scan("app.config");
        ctx.refresh();

        RestaurantService restaurantService = (RestaurantService) ctx.getBean("restaurantService");
        CacheManager cacheManager = (CacheManager) ctx.getBean("cacheManager");
        restaurantService.getById(1);
        restaurantService.getById(1);

    }

}
