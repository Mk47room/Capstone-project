package com.main.library.mapper;

import com.main.library.dto.request.BookReqDto;
import com.main.library.dto.response.BookRespDto;
import com.main.library.model.Book;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    public static Book convertDtoToBook(BookReqDto bookReqDto){
        Book book = new Book();

        book.setTitle(bookReqDto.title());
        book.setIsbn(bookReqDto.isbn());
        book.setPublicationYear(bookReqDto.publicationYear());

        return book;
    }

    public static BookRespDto convertBookToDto(Book book){
        return new BookRespDto(
                book.getId(),
                book.getTitle(),
                book.getIsbn(),
                book.getPublicationYear(),
                book.getAuthor().getName()
        );
    }
}
