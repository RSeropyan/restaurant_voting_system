package app.service.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

public class ValidationUtil {

    private static final Logger logger = LoggerFactory.getLogger(ValidationUtil.class);

    public static void checkNotNullId(Integer id) {
        logger.info("Checking id value = {} for NotNull constraint.", id);
        if (id == null) {
            throw new IllegalArgumentException("The entity id must not be null.");
        }
    }

    public static void checkNullId(Integer id) {
        logger.info("Checking id value = {} for Null constraint.", id);
        if (id != null) {
            throw new IllegalArgumentException("The entity id must be null.");
        }
    }

    public static <T> void checkNotNullInstance(T entity) {
        logger.info("Checking entity instance for NotNull constraint.");
        if (entity == null) {
            throw new IllegalArgumentException("The entity must not be null.");
        }
    }

    public static <T> void checkNotNullProperties(T entity) {
        Field[] fields = entity.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                if(!field.getName().equals("id") && !field.getName().equals("restaurant")) {
                    Object o = field.get(entity);
                    logger.info("Checking entity property {} = {} for NotNull constraint.", field.getName(), o);
                    if (o == null) {
                        throw new IllegalArgumentException("The entity's properties must not be null (except id and restaurant).");
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

}
