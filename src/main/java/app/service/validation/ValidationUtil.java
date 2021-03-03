package app.service.validation;

import app.service.exceptions.EntityPropertiesValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ValidationUtil {

    private static final Validator validator;
    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }

    private static final Logger logger = LoggerFactory.getLogger(ValidationUtil.class);

    public static final String ENTITY_ID_MUST_NOT_BE_NULL_MESSAGE = "Entity id must not be null.";
    public static final String ENTITY_ID_MUST_BE_NULL_MESSAGE = "Entity id must be null.";
    public static final String ENTITY_INSTANCE_MUST_NOT_BE_NULL_MESSAGE = "Entity instance must not be null.";
    public static final String ENTITY_PROPERTIES_NOT_VALID_MESSAGE = "Failed to validate entity properties.";

    public static void checkNotNullEntityId(Integer id) {
        logger.info("Checking id value = {} for NotNull constraint.", id);
        if (id == null) {
            throw new IllegalArgumentException(ENTITY_ID_MUST_NOT_BE_NULL_MESSAGE);
        }
    }

    public static void checkNullEntityId(Integer id) {
        logger.info("Checking id value = {} for Null constraint.", id);
        if (id != null) {
            throw new IllegalArgumentException(ENTITY_ID_MUST_BE_NULL_MESSAGE);
        }
    }

    public static <T> void checkNotNullEntityInstance(T entity) {
        logger.info("Checking entity instance for NotNull constraint.");
        if (entity == null) {
            throw new IllegalArgumentException(ENTITY_INSTANCE_MUST_NOT_BE_NULL_MESSAGE);
        }
    }

    public static <T> void validateEntityProperties(T entity) {
        logger.info("Checking entity properties' constraints.");
        Set<ConstraintViolation<T>> violations = validator.validate(entity);
        List<String> errors = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());
        if (!errors.isEmpty()) {
            throw new EntityPropertiesValidationException(ENTITY_PROPERTIES_NOT_VALID_MESSAGE, errors);
        }
    }

}
