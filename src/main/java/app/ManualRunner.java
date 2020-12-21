package app;

import app.entity.Restaurant;
import app.service.RestaurantService;
import app.service.RestaurantVotingService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ManualRunner {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
//        ctx.getEnvironment().setActiveProfiles("dev");
        ctx.scan("app.config");
        ctx.refresh();

        RestaurantService restaurantService = (RestaurantService) ctx.getBean("restaurantService");
        RestaurantVotingService restaurantVotingService = (RestaurantVotingService) ctx.getBean("restaurantVotingService");

        restaurantVotingService.voteForRestaurantById(1);

    }

}
