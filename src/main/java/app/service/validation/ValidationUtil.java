package app.service.validation;

import app.entity.Meal;
import app.entity.Restaurant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

public class ValidationUtil {

    private static final Logger logger = LoggerFactory.getLogger(ValidationUtil.class);

    public static final String MESSAGE_checkNotNullId = "Entity id must not be null.";
    public static final String MESSAGE_checkNullId = "Entity id must be null.";
    public static final String MESSAGE_checkNotNullInstance = "Entity instance must not be null.";
    public static final String MESSAGE_checkNotNullRestaurantProperties = "Properties of Restaurant entity must not be null (except id and meals).";
    public static final String MESSAGE_checkNotNullMealProperties = "Properties of Meal entity must not be null (except id and restaurant).";

    public static void checkNotNullEntityId(Integer id) {
        logger.info("Checking id value = {} for NotNull constraint.", id);
        if (id == null) {
            throw new IllegalArgumentException(MESSAGE_checkNotNullId);
        }
    }

    public static void checkNullEntityId(Integer id) {
        logger.info("Checking id value = {} for Null constraint.", id);
        if (id != null) {
            throw new IllegalArgumentException(MESSAGE_checkNullId);
        }
    }

    public static <T> void checkNotNullEntityInstance(T entity) {
        logger.info("Checking entity instance for NotNull constraint.");
        if (entity == null) {
            throw new IllegalArgumentException(MESSAGE_checkNotNullInstance);
        }
    }

    public static void checkNotNullRestaurantEntityProperties(Restaurant entity) {
        Field[] fields = entity.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                if(!field.getName().equals("id") &&
                   !field.getName().equals("meals")) {
                    Object o = field.get(entity);
                    logger.info("Checking property {} = {} of Restaurant entity for NotNull constraint.", field.getName(), o);
                    if (o == null) {
                        throw new IllegalArgumentException(MESSAGE_checkNotNullRestaurantProperties);
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public static <T> void checkNotNullMealEntityProperties(Meal entity) {
        Field[] fields = entity.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                if(!field.getName().equals("id") &&
                        !field.getName().equals("restaurant")) {
                    Object o = field.get(entity);
                    logger.info("Checking property {} = {} of Meal entity for NotNull constraint.", field.getName(), o);
                    if (o == null) {
                        throw new IllegalArgumentException(MESSAGE_checkNotNullMealProperties);
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
