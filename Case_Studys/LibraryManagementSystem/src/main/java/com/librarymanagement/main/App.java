package com.librarymanagement.main;

import com.librarymanagement.config.AppConfig;
import com.librarymanagement.dto.BookRespDto;
import com.librarymanagement.enums.Genre;
import com.librarymanagement.model.Author;
import com.librarymanagement.model.Book;
import com.librarymanagement.service.AuthorService;
import com.librarymanagement.service.BookService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        BookService bookService = context.getBean(BookService.class);
        AuthorService authorService = context.getBean(AuthorService.class);
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("-------Library Management------");
            System.out.println("1. Add book ");
            System.out.println("2. Search book by Id");
            System.out.println("3. Display all books");
            System.out.println("0. Exit.");
            switch (sc.nextInt()) {
                case 1 -> {
                    //  task 1. insert book

                    sc.nextLine();//hold the console
                    Book book = new Book();

                    //  a. get details:
                    System.out.println("Enter the title of the book: ");
                    book.setTitle(sc.nextLine());

                    System.out.println("Select the genre: ");
                    Arrays.stream(Genre.values()).forEach(System.out::println);
                    book.setGenre(Genre.valueOf(sc.next().toUpperCase()));

                    System.out.println("Enter the publish year: ");
                    book.setPublishedYear(sc.nextInt());

                    System.out.println("Select the author name: ");
                    sc.nextLine();//hold the console...

                    // a1. show author list:
                    List<String> listAuthor = authorService.getAllAuthors();
                    listAuthor.forEach(System.out::println);
                    String authorName = sc.nextLine();

                    // insert to service:
                    bookService.insertBook(book, authorName);
                    System.out.println("Book Added to Library..");
                    break;
                }
                case 2 -> {
                    //  task 2. read single book by id
                    System.out.println("----- Search Book ----");
                    System.out.println("Enter the book id: ");
                    Book book = bookService.searchBookById(sc.nextInt());
                    System.out.println(book);
                    break;
                }
                case 3 -> {
                    //  task 3. read all books with author and member info
                    System.out.println("------Entire Books-----");
                    List<BookRespDto> listAllBook = bookService.findAll();
                    listAllBook.forEach(System.out::println);
                }
                case 0 -> {
                    System.out.println("Thank you ...");
                    return;
                }
                default -> {
                    System.out.println("Invalid option returning...");
                    return;
                }
            }
        }
    }
}
