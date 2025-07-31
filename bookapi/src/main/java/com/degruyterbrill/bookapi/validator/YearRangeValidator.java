package com.degruyterbrill.bookapi.validator;

import com.degruyterbrill.bookapi.dto.BookListRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

/**
 * Validator for the {@link ValidYearRange} annotation.
 * Checks if 'year_from' is less than or equal to 'year_to' in a {@link BookListRequest}.
 */
public class YearRangeValidator implements ConstraintValidator<ValidYearRange, BookListRequest> {

    @Override
    public boolean isValid(BookListRequest request, ConstraintValidatorContext context) {
        if (request == null) {
            return true;
        }

        String yearFrom = request.getYear_from();
        String yearTo = request.getYear_to();

        if (StringUtils.hasText(yearFrom) && StringUtils.hasText(yearTo)) {
            try {
                int from = Integer.parseInt(yearFrom);
                int to = Integer.parseInt(yearTo);
                return from <= to;
            } catch (NumberFormatException e) {
                // Let other validators handle non-numeric input.
                // This validator only checks the range logic.
                return true;
            }
        }
        return true;
    }
} 