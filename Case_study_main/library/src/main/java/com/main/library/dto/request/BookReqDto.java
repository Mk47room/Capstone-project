package com.main.library.dto.request;

import jakarta.validation.constraints.NegativeOrZero;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BookReqDto(

        @NotNull(message = "Title is Required")
        @NotBlank(message = "Title is Required")
        String title,

        @NotNull(message = "ISBN is required")
        Long isbn,

        @NotNull(message = "Publication Year is required")
        Long publicationYear,

        @NotNull(message = "Author id is Required")
        Long authorId
) {
}
