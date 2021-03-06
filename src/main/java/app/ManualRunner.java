package app;

import app.entity.Meal;
import app.service.RestaurantService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;

public class ManualRunner {

//    public static void main(String[] args) {
//        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
//        ctx.scan("app.config");
//        ctx.refresh();
//        PlatformTransactionManager transactionManager = ctx.getBean(PlatformTransactionManager.class);
//        RestaurantService restaurantService = ctx.getBean(RestaurantService.class);
//        execute(transactionManager, restaurantService);
//    }
//
//    private static void execute(PlatformTransactionManager transactionManager, RestaurantService restaurantService) {
//        DefaultTransactionDefinition txDefinition = new DefaultTransactionDefinition();
//        TransactionStatus status = transactionManager.getTransaction(txDefinition);
//        try {
//            List<Meal> meals = restaurantService.getRestaurantById(2).getMeals();
//            meals.forEach(meal -> System.out.println(meal.toString() + " - " + meal.getRestaurant().getName()));
//            transactionManager.commit(status);
//        } catch (Exception e) {
//            transactionManager.rollback(status);
//            throw e;
//        }
//    }

}
