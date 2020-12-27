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
    public static Restaurant testNewRestaurant;
    public static Meal meal1;
    public static Meal newMeal;

    static {
        initializeTestData();
    }

    public static void initializeTestData() {

        testRestaurant1 = new Restaurant("Marcellis", 3, null);
        testRestaurant1.setId(1);
        meal1 = new Meal("Ceaser Salad", MealCategory.SALAD, 350);
        meal1.setId(1);
        meal1.setRestaurant(testRestaurant1);
        Meal meal2 = new Meal("Tomato Soup", MealCategory.SOUP, 290);
        meal2.setId(2);
        meal2.setRestaurant(testRestaurant1);
        Meal meal3 = new Meal("Four Seasons pizza", MealCategory.MAIN, 560);
        meal3.setId(3);
        meal3.setRestaurant(testRestaurant1);
        Meal meal4 = new Meal("Apple pie", MealCategory.DESERT, 250);
        meal4.setId(4);
        meal4.setRestaurant(testRestaurant1);
        Meal meal5 = new Meal("Latte", MealCategory.DRINK, 350);
        meal5.setId(5);
        meal5.setRestaurant(testRestaurant1);
        List<Meal> meals1 = Arrays.asList(meal1, meal2, meal3, meal4, meal5);
        testRestaurant1.setMeals(meals1);

        testRestaurant2 = new Restaurant("Phalli Khinkali", 9, null);
        testRestaurant2.setId(2);
        Meal meal6 = new Meal("Georgian Salad", MealCategory.SALAD, 290);
        meal6.setId(6);
        meal6.setRestaurant(testRestaurant2);
        Meal meal7 = new Meal("Kharcho Soup", MealCategory.SOUP, 310);
        meal7.setId(7);
        meal7.setRestaurant(testRestaurant2);
        Meal meal8 = new Meal("Chicken Kebab", MealCategory.MAIN, 360);
        meal8.setId(8);
        meal8.setRestaurant(testRestaurant2);
        Meal meal9 = new Meal("Chocolate", MealCategory.DESERT, 200);
        meal9.setId(9);
        meal9.setRestaurant(testRestaurant2);
        Meal meal10 = new Meal("Tea", MealCategory.DRINK, 250);
        meal10.setId(10);
        meal10.setRestaurant(testRestaurant2);
        List<Meal> meals2 = Arrays.asList(meal6, meal7, meal8, meal9, meal10);
        testRestaurant2.setMeals(meals2);

        // restaurant for testing CREATE new restaurant functionality
        testNewRestaurant = new Restaurant("Burger King", 0, null);
        testNewRestaurant.setMeals(new ArrayList<>());

        // meal for testing CREATE new meal functionality
        newMeal = new Meal("Margarita pizza", MealCategory.MAIN, 420);
    }

}
