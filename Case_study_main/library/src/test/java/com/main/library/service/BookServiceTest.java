package com.main.library.service;

import com.main.library.dto.request.BookReqDto;
import com.main.library.dto.request.BookUpdateReqDto;
import com.main.library.dto.response.BookRespDto;
import com.main.library.model.Author;
import com.main.library.model.Book;
import com.main.library.repository.AuthorRepository;
import com.main.library.repository.BookRepository;
import jakarta.persistence.ManyToOne;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;
    @Mock
    private AuthorRepository authorRepository;

    @Test
    public void addBookTest(){
        Author author = new Author();
        author.setId(1L);

        BookReqDto bookRespDto = new BookReqDto("Title",234L,1995L,1L);
        when(authorRepository.findById(author.getId())).thenReturn(Optional.of(author));
        when(bookRepository.save(any(Book.class))).thenReturn(any(Book.class));

        bookService.addBook(bookRespDto);

    }

    @Test
    public void getAllBooksTest(){
        Author author = new Author(1L,"hendry",null);
        Book book = new Book(1L,"harry",244L,1999L,author);
        Book book2 = new Book(2L,"Potter",243L,2019L,author);

        when(bookRepository.findAll()).thenReturn(List.of(book, book2));

        List<BookRespDto> list = bookService.getAllBooks();
        assertEquals(2,list.size());
        verify(bookRepository, times(1)).findAll();

    }

    @Test
    public void getBookTest() {
        Long isbn = 234L;
        BookRespDto dto = new BookRespDto(1L, "Harry", isbn, 1994L, "Robert");
        when(bookRepository.getBook(isbn)).thenReturn(dto);

        BookRespDto dto2 = bookService.getBook(isbn);

        assertEquals(dto, dto2);
        verify(bookRepository, times(1)).getBook(isbn);
    }

    @Test
    public void deleteBookTest() {
        Author author = new Author(1L,"hendry",null);
        Long isbn = 234L;
        Book book = new Book(1L,"Harry", 234L,1995L,author);
        when(bookRepository.getBookByIsbn(isbn)).thenReturn(book);
        bookService.deleteBook(isbn);

        verify(bookRepository, times(1)).delete(book);
    }

    @Test
    public void updateBookTest() {
        Author author = new Author(2L,"hendry",null);
        Book book = new  Book(1L,"Harry", 234L,1995L,author);

        BookUpdateReqDto dto = new BookUpdateReqDto("NewTitle",123L,2000L,2L);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(authorRepository.findById(author.getId())).thenReturn(Optional.of(author));

        bookService.updateBook(1L,dto);
        assertEquals("NewTitle",book.getTitle());

        verify(bookRepository, times(1)).save(book);
    }

}
