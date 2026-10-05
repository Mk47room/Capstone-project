package com.librarymanagement.dto;

import com.librarymanagement.enums.Genre;

import java.time.LocalDate;

public record BookRespDto(
        long bookId,
        String title,
        Genre genre,
        String authorName,
        Long memberId,
        String email,
        LocalDate joinedAt
) {
}
