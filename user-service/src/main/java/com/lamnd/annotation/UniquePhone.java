package com.lamnd.annotation;

import com.lamnd.annotation.validator.UniquePhoneValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = UniquePhoneValidator.class)
public @interface UniquePhone {
    String message() default "Phone number already exists"; // default error message when validation fails
    Class<?>[] groups() default {}; // for grouping validations
    Class<? extends Payload>[] payload() default {}; // for carrying metadata
}
