package app.service.validation;

import app.service.exceptions.EntityValidationException;
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

    public static final String MESSAGE_checkNotNullId = "Entity id must not be null.";
    public static final String MESSAGE_checkNullId = "Entity id must be null.";
    public static final String MESSAGE_checkNotNullInstance = "Entity instance must not be null.";

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

    public static <T> void validateEntityProperties(T entity) {
        Set<ConstraintViolation<T>> violations = validator.validate(entity);
        List<String> errors = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());
        throw new EntityValidationException(errors);
    }

}
