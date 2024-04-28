package com.tw.bookmanagementserver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureJsonTesters
class BookControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private JacksonTester<List<Book>> booksJson;


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
    void should_return_multi_books() throws IOException {
        final List<Book> books = List.of(buildBook());
        bookRepository.saveAll(books);
        final ResponseEntity<String> responseEntity = restTemplate.getForEntity("/books", String.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
        final String fetchedBooks = responseEntity.getBody();
        assertThat(booksJson.parseObject(fetchedBooks)).isEqualTo(books);
    }

    @Test
    void should_return_created_when_add_book() {
        final Book book = buildBook();
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
        final Book book = buildBook();
        bookRepository.save(book);
        ResponseEntity<Book> responseEntity = restTemplate.getForEntity("/books/{id}", Book.class, book.getId());

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

    @Test
    void should_throw_exceptions_when_id_not_exist() {
        ResponseEntity<Book> responseEntity = restTemplate.getForEntity("/books/{id}", Book.class, 123L);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
    }

    @Test
    void should_update_book_when_id_exist() {
        final Book book = buildBook();
        bookRepository.save(book);
        Book updatedBook = Book.builder().id(book.getId()).title("update").author("update author").year(2000).isbn("1111").build();
        HttpEntity<Book> requestEntity = new HttpEntity<>(updatedBook);
        String url = "/books/" + book.getId();
        ResponseEntity<Book> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Book.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        final Book fetchedBook = responseEntity.getBody();
        assertThat(fetchedBook).isNotNull();
        assertThat(fetchedBook.getTitle()).isEqualTo(updatedBook.getTitle());
        assertThat(fetchedBook.getAuthor()).isEqualTo(updatedBook.getAuthor());
        assertThat(fetchedBook.getYear()).isEqualTo(updatedBook.getYear());
        assertThat(fetchedBook.getIsbn()).isEqualTo(updatedBook.getIsbn());
    }

    @Test
    void should_throw_exceptions_when_update_and_id_not_exist() {
        Book updatedBook = buildBook();
        HttpEntity<Book> requestEntity = new HttpEntity<>(updatedBook);
        String url = "/books/" + updatedBook.getId();
        ResponseEntity<Book> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Book.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
    }

    @Test
    void should_delete_book() {
        final Book book = buildBook();
        bookRepository.save(book);
        String url = "/books/" + book.getId();
        ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.DELETE, null, Long.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(bookRepository.findById(book.getId())).isEmpty();
    }

    private static Book buildBook() {
        return Book.builder().title("new book").author("new author").year(2024).isbn("0000").build();
    }
}
