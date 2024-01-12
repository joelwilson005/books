package com.joel.books.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.joel.books.model.Book;
import com.joel.books.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BookServiceImpl implements BookService{

    private final BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book findBookById(Long id) {
        return this.bookRepository.findById(id).orElseThrow();
    }

    @Override
    public Book findBookByTitle(String title) {
        return this.bookRepository.findBookByTitle(title).orElseThrow();
    }

    @Override
    public List<Book> getAllBooks() {
        return this.bookRepository.findAll();
    }

    @Override
    public List<Book> findBooksByAuthor(String author) {
        return this.bookRepository.findBooksByAuthor(author).stream().toList();
    }

    @Override
    public Book addBook(Book book) {
        return this.bookRepository.save(book);
    }

    @Override
    public Book findBookByDatePublished(Date date) {
        return this.bookRepository.findBookByDatePublished(date).orElseThrow();
    }

    @Override
    public boolean deleteBook(Long id) {
        try {
            Book bookToBeDeleted = this.bookRepository.findById(id).orElseThrow();
            this.bookRepository.delete(bookToBeDeleted);
            return true;
        } catch (IllegalArgumentException | NoSuchElementException e) {
            return false;
        }
    }

    @Override
    public Book applyPatchToBook(JsonPatch patch, Book book) throws JsonPatchException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode patched = patch.apply(objectMapper.convertValue(book, JsonNode.class));
        return objectMapper.treeToValue(patched, Book.class);

    }

    @Override
    public Book updateBook(Book book) {
        return this.bookRepository.save(book);
    }
}
