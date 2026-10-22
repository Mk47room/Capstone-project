package com.main.library.repository;

import com.main.library.dto.response.BookRespDto;
import com.main.library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book,Long> {

    @Query("""
select b.id as bookId,
b.title as title,
b.isbn as isbn,
b.publicationYear as publicationYear,
a.name as authorName
from Book b
JOIN b.author a
where b.isbn = ?1
""")
    BookRespDto getBook(Long isbn);

    @Query("""
select b
from Book b
where b.isbn = ?1
""")
    Book getBookByIsbn(Long isbn);
}
