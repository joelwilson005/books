package com.joel.books.repository;

import com.joel.books.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findBookByTitle(String title);
    Optional<Book> findBookByDatePublished(Date date);
    Optional<Book> findBooksByAuthor(String author);
}
