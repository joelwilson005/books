package com.joel.books.service;

import com.joel.books.model.Book;
import com.joel.books.repository.BookRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

public class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository; // This is being mocked because it's an external dependency of BookService
    private BookService bookService;
    private AutoCloseable autoCloseable;
    Book book;

    @BeforeEach
    void setUp() throws ParseException {
        /*The MockitoAnnotations.openMocks() method
        returns an instance of AutoClosable which can
        be used to close the resource after the test. */
        autoCloseable = MockitoAnnotations.openMocks(this);
        bookService = new BookServiceImpl(bookRepository);
        book = Book.builder()
                .title("Harry Potter and the Sorcerer's Stone")
                .Id(1L)
                .datePublished(new SimpleDateFormat("yyyy-MM-dd").parse("1998-09-01"))
                .author("JK Rowling")
                .build();
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void testAddNewBook() {
        // Mocking the Book and BookRepository classes
        // This is creating mock objects for these classes, which allows you to simulate their behavior.
        mock(Book.class);
        mock(BookRepository.class);

        /*
        Here we are mocking the value returned by bookRepository
        We're telling Mockito to return the book value whenever the save
        method is called on the bookRepository class.
        This allows us to simulate behaviour.
         */
        when(bookRepository.save(book)).thenReturn(book);

        /*
        Since Mockito is mocking the BookRepository interface,
        we're checking here to see if the book object returned by the
        bookService matches our book.

         */
        assertThat(bookService.addBook(book)).isEqualTo(book);
    }

    @Test
    void testFindBookById_Found() {
        mock(Book.class);
        mock(BookRepository.class);

        when(bookRepository.findById(1L)).thenReturn(Optional.ofNullable(book));
        assertThat(bookService.findBookById(1L)).isEqualTo(book);
    }

    @Test
    void testFindBookById_NotFound_ThenExceptionThrown() {
        mock(Book.class);
        mock(BookRepository.class);

        when(bookRepository.findById(2L)).thenThrow(NoSuchElementException.class);
        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> {
            bookService.findBookById(2L);
        });
    }

    @Test
    void testFindBookByTitle_Found() {
        mock(Book.class);
        mock(BookRepository.class);

        when(bookRepository.findBookByTitle("Harry Potter and the Sorcerer's Stone"))
                .thenReturn(Optional.ofNullable(book));
        assertThat(bookService.findBookByTitle("Harry Potter and the Sorcerer's Stone")).isEqualTo(book);
    }

    @Test
    void testFindBookByTitle_NotFound_ThenExceptionThrown() {
        mock(Book.class);
        mock(BookRepository.class);

        when(bookRepository.findBookByTitle("Twilight")).thenThrow(NoSuchElementException.class);
        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> {
            bookService.findBookByTitle("Twilight");
        });
    }

    @Test
    void testGetAllBooks_WhenThereAreBooks() {
        mock(Book.class);
        mock(BookRepository.class);

        when(bookRepository.findAll()).thenReturn(Stream.of(book).toList());
        assertThat(bookService.getAllBooks().get(0).getId()).isEqualTo(book.getId());
    }

    @Test
    void testGetAllBooks_WhenThereAreNoBooks() {
        mock(Book.class);
        mock(BookRepository.class);

        when(bookRepository.findAll()).thenReturn(new ArrayList<>());
        assertThat(bookService.getAllBooks()).isEmpty();
    }

    @Test
    void testDeleteBook() {
        mock(Book.class);
        mock(BookRepository.class, CALLS_REAL_METHODS);
        doAnswer(Answers.CALLS_REAL_METHODS).when(bookRepository).deleteById(any());

        assertThat(bookService.deleteBook(1L)).isEqualTo(false);
    }
}
