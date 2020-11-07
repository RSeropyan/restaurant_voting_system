package app.service.testdata;

import app.entity.Meal;
import app.entity.MealCategory;
import app.entity.Restaurant;

import java.util.Arrays;
import java.util.List;

public class RestaurantTestData {

    public static final Restaurant testRestaurant1;
    private static final Meal meal1;
    private static final Meal meal2;
    private static final Meal meal3;
    private static final Meal meal4;
    private static final Meal meal5;

    static {
        testRestaurant1 = new Restaurant(1, "Marcellis", 3);
        meal1 = new Meal(1, "Ceaser Salad", MealCategory.SALAD, 350, testRestaurant1);
        meal2 = new Meal(2, "Tomato Soup", MealCategory.SOUP, 290, testRestaurant1);
        meal3 = new Meal(3, "Four Seasons pizza", MealCategory.MAIN, 560, testRestaurant1);
        meal4 = new Meal(4, "Apple pie", MealCategory.DESERT, 250, testRestaurant1);
        meal5 = new Meal(5, "Latte", MealCategory.DRINK, 350, testRestaurant1);
        List<Meal> meals = Arrays.asList(meal1, meal2, meal3, meal4, meal5);
        testRestaurant1.setMeals(meals);
    }

}
