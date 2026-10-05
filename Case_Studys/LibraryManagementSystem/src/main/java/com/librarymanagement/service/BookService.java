package com.librarymanagement.service;

import com.librarymanagement.Exception.NameNotfoundException;
import com.librarymanagement.Repository.AuthorRepository;
import com.librarymanagement.Repository.BookRepository;
import com.librarymanagement.dto.BookRespDto;
import com.librarymanagement.enums.Status;
import com.librarymanagement.mapper.BookMapper;
import com.librarymanagement.model.Author;
import com.librarymanagement.model.Book;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public void insertBook(Book book,String authorName) {
        //get author from db
        List<Author> optional = authorRepository.getAuthorByName(authorName);
        if(optional.isEmpty())
            throw new NameNotfoundException("Author not found");
        Author author = optional.getFirst();

        //map author with book;
        book.setAuthor(author);
        book.setStatus(Status.AVAILABLE);

        //insert book;
        bookRepository.insertBook(book);
    }

    public Book searchBookById(int bookId) {
        return bookRepository.searchBookById(bookId);
    }

    public List<BookRespDto> findAll() {
        List<Book> books = bookRepository.findAll();
        return books
                .stream()
                .map(BookMapper :: mapBookToDto)
                .toList();
    }
}
