package com.tw.bookmanagementserver;

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
        final Book createdBook = responseEntity.getBody();
        assertThat(createdBook).isNotNull();
        assertThat(createdBook.getId()).isPositive();
        assertThat(createdBook.getTitle()).isEqualTo(book.getTitle());
        assertThat(createdBook.getAuthor()).isEqualTo(book.getAuthor());
        assertThat(createdBook.getYear()).isEqualTo(book.getYear());
        assertThat(createdBook.getIsbn()).isEqualTo(book.getIsbn());
    }

}
