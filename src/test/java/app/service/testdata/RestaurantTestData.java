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

    public static final Restaurant testRestaurant2;
    private static final Meal meal6;
    private static final Meal meal7;
    private static final Meal meal8;
    private static final Meal meal9;
    private static final Meal meal10;

    static {
        testRestaurant1 = new Restaurant(1, "Marcellis", 3);
        meal1 = new Meal(1, "Ceaser Salad", MealCategory.SALAD, 350, testRestaurant1);
        meal2 = new Meal(2, "Tomato Soup", MealCategory.SOUP, 290, testRestaurant1);
        meal3 = new Meal(3, "Four Seasons pizza", MealCategory.MAIN, 560, testRestaurant1);
        meal4 = new Meal(4, "Apple pie", MealCategory.DESERT, 250, testRestaurant1);
        meal5 = new Meal(5, "Latte", MealCategory.DRINK, 350, testRestaurant1);
        List<Meal> meals1 = Arrays.asList(meal1, meal2, meal3, meal4, meal5);
        testRestaurant1.setMeals(meals1);

        testRestaurant2 = new Restaurant(2, "Phalli Khinkali", 9);
        meal6 = new Meal(6, "Georgian Salad", MealCategory.SALAD, 290, testRestaurant2);
        meal7 = new Meal(7, "Kharcho Soup", MealCategory.SOUP, 310, testRestaurant2);
        meal8 = new Meal(8, "Chicken Kebab", MealCategory.MAIN, 360, testRestaurant2);
        meal9 = new Meal(9, "Chocolate", MealCategory.DESERT, 200, testRestaurant2);
        meal10 = new Meal(10, "Tea", MealCategory.DRINK, 250, testRestaurant2);
        List<Meal> meals2 = Arrays.asList(meal6, meal7, meal8, meal9, meal10);
        testRestaurant2.setMeals(meals2);
    }

}
