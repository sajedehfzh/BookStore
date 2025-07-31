package com.degruyterbrill.bookapi.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Validation annotation to ensure that 'year_from' is less than or equal to 'year_to'.
 * This is a class-level constraint, applied to classes that contain both year fields.
 */
@Constraint(validatedBy = YearRangeValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidYearRange {
    String message() default "year_from must be less than or equal to year_to";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
} 