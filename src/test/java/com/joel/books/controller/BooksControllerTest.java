package com.joel.books.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.joel.books.model.Book;
import com.joel.books.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BooksController.class)
class BooksControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BookService bookService;
    private Book bookOne;
    private Book bookTwo;
    List<Book> bookList = new ArrayList<>();

    @BeforeEach
    void setUp() throws ParseException {
        bookOne = Book.builder()
                .Id(1L)
                .title("Hunger Games")
                .author("Suzanne Collins")
                .datePublished(new SimpleDateFormat("yyyy-MM-dd").parse("2007-09-15"))
                .build();
        bookTwo = Book.builder()
                .Id(1L)
                .title("Twilight")
                .author("Stephenie Meyer")
                .datePublished(new SimpleDateFormat("yyyy-MM-dd").parse("2005-09-27"))
                .build();
        bookList.add(bookOne);
        bookList.add(bookTwo);
    }


    @Test
    void testGetAllBooks() throws Exception {
        when(bookService.getAllBooks()).thenReturn(bookList);
        this.mockMvc.perform(get("/books"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void testGetBookById() throws Exception {
        when(bookService.findBookById(1L)).thenReturn(bookOne);

        this.mockMvc.perform(get("/books/1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void testAddNewBook() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
        String jsonRequest = writer.writeValueAsString(bookOne);

        when(bookService.addBook(bookOne)).thenReturn(bookOne);
        this.mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(jsonRequest))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void testDeleteBookById() throws Exception {
        when(bookService.deleteBook(1L)).thenReturn(true);
        this.mockMvc.perform(delete("/books/1"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}