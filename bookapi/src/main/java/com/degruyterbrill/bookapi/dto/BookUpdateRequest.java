package com.degruyterbrill.bookapi.dto;

import com.degruyterbrill.bookapi.model.BookStatus;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class BookUpdateRequest {

    private String isbn;

    private String title;

    private String subtitle;

    private String copyrightYear;

    private BookStatus status;
}
