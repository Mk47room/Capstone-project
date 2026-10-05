package com.librarymanagement.mapper;

import com.librarymanagement.dto.BookRespDto;
import com.librarymanagement.model.Book;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneOffset;

@Component
public class BookMapper {

    public static BookRespDto mapBookToDto(Book book){
        return new BookRespDto(
                book.getId(),
                book.getTitle(),
                book.getGenre(),
                book.getAuthor().getName(),
                book.getMember() == null?
                        null : book.getMember().getId(),
                book.getMember() == null?
                        null : book.getMember().getEmail(),
                book.getMember() == null?
                        null : LocalDate.ofInstant(book.getMember().getJoinedAt(), ZoneOffset.UTC)
        );
    }
}
