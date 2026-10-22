package com.main.library.controller;

import com.main.library.dto.request.BookReqDto;
import com.main.library.dto.request.BookUpdateReqDto;
import com.main.library.dto.response.BookRespDto;
import com.main.library.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping("/api/book/add")
    public void addBook(@Valid @RequestBody BookReqDto bookReqDto){
        bookService.addBook(bookReqDto);
    }

    @GetMapping("/api/book/all")
    public List<BookRespDto> getAllBooks(){
        return bookService.getAllBooks();
    }

    @GetMapping("/api/book/{isbn}")
    public BookRespDto getBook(@PathVariable Long isbn){
        return bookService.getBook(isbn);
    }

    @PutMapping("/api/book/update/{bookId}")
    public void updateBook(@PathVariable Long bookId,
                           @RequestBody BookUpdateReqDto bookReqDto){
        bookService.updateBook(bookId,bookReqDto);
    }

    @DeleteMapping("/api/book/delete/{isbn}")
    public void deleteBook(@PathVariable Long isbn){
        bookService.deleteBook(isbn);
    }


}
