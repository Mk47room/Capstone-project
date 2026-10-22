package com.main.library.dto.request;

import jakarta.validation.constraints.NotNull;

public record BookUpdateReqDto(
        String title,
        Long isbn,
        Long publicationYear,
        Long authorId
) {
}
