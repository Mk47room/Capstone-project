package com.main.library.service;

import com.main.library.dto.request.BookReqDto;
import com.main.library.dto.request.BookUpdateReqDto;
import com.main.library.dto.response.BookRespDto;
import com.main.library.exception.InvalidCallException;
import com.main.library.exception.ResourceNotFoundException;
import com.main.library.mapper.BookMapper;
import com.main.library.model.Author;
import com.main.library.model.Book;
import com.main.library.repository.AuthorRepository;
import com.main.library.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    public void addBook(BookReqDto bookReqDto) {
        //1. Find author
        Author author = authorRepository.findById(bookReqDto.authorId())
                .orElseThrow(()-> new ResourceNotFoundException("Author not found"));

        //2. create and map book
        Book book = BookMapper.convertDtoToBook(bookReqDto);

        //3. set author
        book.setAuthor(author);

        //4. save in db
        bookRepository.save(book);
    }

    public List<BookRespDto> getAllBooks() {
        List<Book> list = bookRepository.findAll();

        return list.stream()
                .map(BookMapper::convertBookToDto)
                .toList();

    }

    public BookRespDto getBook(Long isbn) {
        return bookRepository.getBook(isbn);
    }

    public void deleteBook(Long isbn)  {
        Book book = bookRepository.getBookByIsbn(isbn);

        if(book == null){
            throw new ResourceNotFoundException("Book not found");
        }
        bookRepository.delete(book);
    }

    public void updateBook(Long bookId, BookUpdateReqDto dto) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(()-> new ResourceNotFoundException("Book not found"));

        boolean flag = false;

        if(!(dto.title() == null) && !(dto.title().trim().isEmpty())) {
            book.setTitle(dto.title());
            flag = true;
        }
        if(!(dto.isbn() == null)) {
            book.setIsbn(dto.isbn());
            flag = true;
        }
        if(!(dto.publicationYear() == null)){
            book.setPublicationYear(dto.publicationYear());
            flag = true;
        }
        if(!(dto.authorId() == null)) {
            Author author = authorRepository.findById(dto.authorId())
                            .orElseThrow(()-> new ResourceNotFoundException("Author not found"));
            book.setAuthor(author);
            flag = true;
        }

        if(!flag)
            throw new InvalidCallException("Cannot perform Update operation");

        bookRepository.save(book);

    }
}
