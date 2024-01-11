package com.joel.books.repository;

import com.joel.books.model.Book;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/*
@DataJpaTest will configure an in-memory database,
ensuring that tests don't interfere with the main database,
hence maintaining the integrity of the data.
 */
@DataJpaTest
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;
    private Book book;

    @BeforeEach
    void setUp() throws ParseException {
        book = Book.builder()
                .title("Hunger Games")
                .author("Suzanne Collins")
                .datePublished(new SimpleDateFormat("yyyy-MM-dd")
                        .parse("2008-09-14"))
                .build();
        bookRepository.save(book);
    }

    @AfterEach
    void tearDown() {
        book = null;
        bookRepository.deleteAll();
    }

    @Test
    void testFindBookByTitle_Found() {
        Book bookInDb = bookRepository.findBookByTitle("Hunger Games").orElseThrow();

        assertThat(bookInDb.getId()).isEqualTo(book.getId());
        assertThat(bookInDb.getTitle()).isEqualTo(book.getTitle());
    }

    @Test
    void testFindBookByTitle_isNotFound_thenExceptionIsThrown() {
        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> {
            bookRepository.findBookByTitle("Twilight").orElseThrow(); // No book with such title in db
        });
    }


    @Test
    void testFindBookByDatePublished_Found() throws ParseException {

        Book bookInDb = bookRepository
                .findBookByDatePublished(new SimpleDateFormat("yyyy-MM-dd")
                        .parse("2008-09-14"))
                .orElseThrow();

        assertThat(bookInDb.getId()).isEqualTo(book.getId());
        assertThat(bookInDb.getDatePublished()).isEqualTo(book.getDatePublished());
    }

    @Test
    void testFindBookByDatePublished_isNotFound_thenExceptionIsThrown() {
        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> {
                    bookRepository
                            .findBookByDatePublished(new SimpleDateFormat("yyyy-MM-dd")
                                    .parse("2023-01-10")) // No book with such published date in db
                            .orElseThrow();
                });
    }
}