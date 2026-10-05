package com.librarymanagement.Repository;

import com.librarymanagement.model.Book;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookRepository {

    @PersistenceContext
    EntityManager entityManager;

    public void insertBook(Book book) {
        entityManager.persist(book);
    }

    public Book searchBookById(int bookId) {
        return entityManager.find(Book.class,bookId);
    }

    public List<Book> findAll() {
        String jpql = "select b from Book b";
        return entityManager.createQuery(jpql,Book.class).getResultList();
    }
}
