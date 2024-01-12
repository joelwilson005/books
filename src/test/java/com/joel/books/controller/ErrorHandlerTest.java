package com.joel.books.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
class ErrorHandlerTest {

    private ErrorHandler errorHandler;
    @BeforeEach
    void setUp() {
        errorHandler = new ErrorHandler();
    }

    @AfterEach
    void tearDown() {
        errorHandler = null;
    }

    @Test
    void testHandleNotFoundException() {
        NoSuchElementException elementException = new NoSuchElementException("Element not found");

        assertThat(errorHandler.handleNotFoundException(elementException))
                .isInstanceOf(ProblemDetail.class)
                .isEqualTo(ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, elementException.getMessage()));

    }
}