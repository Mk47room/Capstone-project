package com.main.library.dto.response;


public record BookRespDto(
        Long bookId,
        String title,
        Long isbn,
        Long publicationYear,
        String authorName
) {
}
