package app.service.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

public class EntityValidator {

    private static final Logger logger = LoggerFactory.getLogger(EntityValidator.class);

    public static boolean checkNotNullId(Integer id) {
        logger.info("Checking id value = {} for NotNull constraint.", id);
        if (id != null) {
            return true;
        }
        else {
            throw new IllegalArgumentException("The entity id must not be null.");
        }
    }

    public static boolean checkNullId(Integer id) {
        logger.info("Checking id value = {} for Null constraint.", id);
        if (id == null) {
            return true;
        }
        else {
            throw new IllegalArgumentException("The entity id must be null.");
        }
    }

    public static <T> boolean checkNotNullInstance(T entity) {
        logger.info("Checking entity instance for NotNull constraint.");
        if (entity != null) {
            return true;
        }
        else {
            throw new IllegalArgumentException("The entity must not be null.");
        }
    }

    public static <T> boolean checkNotNullProperties(T entity) {
        Field[] fields = entity.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                if(!field.getName().equals("id")) {
                    Object o = field.get(entity);
                    logger.info("Checking entity property {} = {} for NotNull constraint.", field.getName(), o);
                    if (o == null) {
                        throw new IllegalArgumentException("The entity's properties must not be null (except id).");
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

}
