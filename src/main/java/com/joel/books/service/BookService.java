package com.joel.books.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
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
    Book applyPatchToBook(JsonPatch patch, Book book) throws JsonPatchException, JsonProcessingException;
    Book updateBook(Book book);
}
