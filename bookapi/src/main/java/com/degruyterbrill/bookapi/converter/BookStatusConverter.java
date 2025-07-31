package com.degruyterbrill.bookapi.converter;

import com.degruyterbrill.bookapi.model.BookStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class BookStatusConverter implements AttributeConverter<BookStatus, String> {

    @Override
    public String convertToDatabaseColumn(BookStatus status) {
        if (status == null) {
            return null;
        }
        return status.getValue();
    }

    @Override
    public BookStatus convertToEntityAttribute(String value) {
        if (value == null) {
            return null;
        }
        return BookStatus.fromValue(value);
    }
} 