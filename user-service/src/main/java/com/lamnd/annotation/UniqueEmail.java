package com.lamnd.annotation;

import com.lamnd.annotation.validator.UniqueEmailValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD}) // This annotation can only be applied to fields
@Retention(RetentionPolicy.RUNTIME) // The annotation will be available at runtime through reflection
@Documented // This annotation will be included in the Javadoc
@Constraint(validatedBy = UniqueEmailValidator.class)
public @interface UniqueEmail {
    String message() default "Email already exists"; // default error message when validation fails
    Class<?>[] groups() default {}; // for grouping validations
    Class<? extends Payload>[] payload() default {}; // for carrying metadata
}
