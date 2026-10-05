package com.librarymanagement.service;

import com.librarymanagement.Repository.AuthorRepository;
import com.librarymanagement.model.Author;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public List<String> getAllAuthors() {
         List<Author> list = authorRepository.getAllAuthors();
         return list
                 .stream()
                 .map(Author::getName)
                 .toList();
    }
}
