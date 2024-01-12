package com.joel.books.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.joel.books.model.Book;
import com.joel.books.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BooksController {
    private final BookService bookService;
    @Autowired
    public BooksController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        var books = this.bookService.getAllBooks();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        var book = this.bookService.findBookById(id);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Book> addNewBook(@RequestBody @Valid Book book) {
        book = this.bookService.addBook(book);
        return new ResponseEntity<>(book, HttpStatus.CREATED);
    }

    @PatchMapping(value = "/{id}", consumes = "application/json-patch+json")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody JsonPatch patch) {
        try {
            Book book = this.bookService.findBookById(id);
            Book patchedBook = this.bookService.applyPatchToBook(patch, book);
            patchedBook = this.bookService.updateBook(patchedBook);
            return new ResponseEntity<>(patchedBook, HttpStatus.OK);
        } catch (JsonPatchException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBookById(Long id) {
        this.bookService.deleteBook(id);
        return new ResponseEntity<>("Book successfully deleted", HttpStatus.OK);
    }
}
