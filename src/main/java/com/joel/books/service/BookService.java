package com.joel.books.service;

import com.joel.books.model.Book;

import java.util.Date;
import java.util.List;

public interface BookService {

    Book findBookById(Long id);
    Book findBookByTitle(String title);
    List<Book> getAllBooks();
    List<Book> findBooksByAuthor(String author);
    Book addBook(Book book);
    Book findBookByDatePublished(Date date);
    boolean deleteBook(Long id);
}
