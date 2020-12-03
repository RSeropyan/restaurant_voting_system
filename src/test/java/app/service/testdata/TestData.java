package app.service.testdata;

import app.entity.Meal;
import app.entity.MealCategory;
import app.entity.Restaurant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestData {

    public static Restaurant testRestaurant1;
    public static Restaurant testRestaurant2;
    public static Restaurant testRestaurant3;

    static {
        initializeTestData();
    }

    public static void initializeTestData() {

        testRestaurant1 = new Restaurant(1, "Marcellis", 3);
        Meal meal1 = new Meal(1, "Ceaser Salad", MealCategory.SALAD, 350, testRestaurant1);
        Meal meal2 = new Meal(2, "Tomato Soup", MealCategory.SOUP, 290, testRestaurant1);
        Meal meal3 = new Meal(3, "Four Seasons pizza", MealCategory.MAIN, 560, testRestaurant1);
        Meal meal4 = new Meal(4, "Apple pie", MealCategory.DESERT, 250, testRestaurant1);
        Meal meal5 = new Meal(5, "Latte", MealCategory.DRINK, 350, testRestaurant1);
        List<Meal> meals1 = Arrays.asList(meal1, meal2, meal3, meal4, meal5);
        testRestaurant1.setMeals(meals1);

        testRestaurant2 = new Restaurant(2, "Phalli Khinkali", 9);
        Meal meal6 = new Meal(6, "Georgian Salad", MealCategory.SALAD, 290, testRestaurant2);
        Meal meal7 = new Meal(7, "Kharcho Soup", MealCategory.SOUP, 310, testRestaurant2);
        Meal meal8 = new Meal(8, "Chicken Kebab", MealCategory.MAIN, 360, testRestaurant2);
        Meal meal9 = new Meal(9, "Chocolate", MealCategory.DESERT, 200, testRestaurant2);
        Meal meal10 = new Meal(10, "Tea", MealCategory.DRINK, 250, testRestaurant2);
        List<Meal> meals2 = Arrays.asList(meal6, meal7, meal8, meal9, meal10);
        testRestaurant2.setMeals(meals2);

        // restaurant for testing CREATE new restaurant functionality
        testRestaurant3 = new Restaurant(null, "Burger King", 0);
        testRestaurant3.setMeals(new ArrayList<>());

    }

}
