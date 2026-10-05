package com.librarymanagement.Repository;

import com.librarymanagement.model.Author;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AuthorRepository {

    @PersistenceContext
    EntityManager entityManager;

    public List<Author> getAllAuthors() {
        String jpql = "select a from Author a";
        return entityManager.createQuery(jpql, Author.class).getResultList();
    }

    public List<Author> getAuthorByName(String authorName) {
        String jpql = "select a from Author a where a.name = ?1";
        return entityManager.createQuery(jpql,Author.class)
                .setParameter(1,authorName)
                .getResultList();
    }
}
