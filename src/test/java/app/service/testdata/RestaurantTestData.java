package app.service.testdata;

import app.entity.Meal;
import app.entity.MealCategory;
import app.entity.Restaurant;

import java.util.Arrays;
import java.util.List;

public class RestaurantTestData {

    private static final Meal meal1;
    private static final Meal meal2;
    private static final Meal meal3;
    private static final Meal meal4;
    private static final Meal meal5;
    public static final Restaurant testDataRestaurant;

    static {
        testDataRestaurant = new Restaurant(1, "Marcellis", 3);
        meal1 = new Meal(1, "Ceaser Salad", MealCategory.SALAD, 350, testDataRestaurant);
        meal2 = new Meal(2, "Tomato Soup", MealCategory.SOUP, 290, testDataRestaurant);
        meal3 = new Meal(3, "Four Seasons pizza", MealCategory.MAIN, 560, testDataRestaurant);
        meal4 = new Meal(4, "Apple pie", MealCategory.DESERT, 250, testDataRestaurant);
        meal5 = new Meal(5, "Latte", MealCategory.DRINK, 350, testDataRestaurant);
        List<Meal> meals = Arrays.asList(meal1, meal2, meal3, meal4, meal5);
        testDataRestaurant.setMeals(meals);
    }

}
