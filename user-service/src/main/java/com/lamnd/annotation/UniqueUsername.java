package com.lamnd.annotation;

import com.lamnd.annotation.validator.UniqueUsernameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = UniqueUsernameValidator.class)
public @interface UniqueUsername {
    String message() default "Username already exists"; // default error message when validation fails
    Class<?>[] groups() default {}; // for grouping validations
    Class<? extends Payload>[] payload() default {}; // for carrying metadata
}
