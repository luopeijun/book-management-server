package com.tw.bookmanagementserver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void setup() {
        bookRepository.deleteAll();
    }
    @Test
    void should_return_empty_book_list() {
        final ResponseEntity<List> responseEntity = restTemplate.getForEntity("/books", List.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
        assertThat(responseEntity.getBody()).isEmpty();
    }

    @Test
    void should_return_created_when_add_book() {
        final Book book = Book.builder().title("new book").author("new author").year(2024).isbn("0000").build();
        final ResponseEntity<Book> responseEntity = restTemplate.postForEntity("/books", book, Book.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
        final Book resBook = responseEntity.getBody();
        assertThat(resBook).isNotNull();
        assertThat(resBook.getId()).isPositive();
        assertThat(resBook.getTitle()).isEqualTo(book.getTitle());
        assertThat(resBook.getAuthor()).isEqualTo(book.getAuthor());
        assertThat(resBook.getYear()).isEqualTo(book.getYear());
        assertThat(resBook.getIsbn()).isEqualTo(book.getIsbn());
    }

    @Test
    void should_return_book_when_id_exists() {
        final Long id = 1L;
        final Book book = Book.builder().id(1L).title("new book").author("new author").year(2024).isbn("0000").build();
        bookRepository.save(book);
        final ResponseEntity<Book> responseEntity = restTemplate.getForEntity("/books".concat("/").concat(id.toString()), Book.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
        final Book resBook = responseEntity.getBody();
        assertThat(resBook).isNotNull();
        assertThat(resBook.getId()).isEqualTo(book.getId());
        assertThat(resBook.getTitle()).isEqualTo(book.getTitle());
        assertThat(resBook.getAuthor()).isEqualTo(book.getAuthor());
        assertThat(resBook.getYear()).isEqualTo(book.getYear());
        assertThat(resBook.getIsbn()).isEqualTo(book.getIsbn());
    }


}
