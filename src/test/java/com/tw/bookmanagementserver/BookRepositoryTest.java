package com.tw.bookmanagementserver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace =  AutoConfigureTestDatabase.Replace.NONE)
@Rollback
class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setup() {
        bookRepository.deleteAll();
    }

    @Test
    void should_return_empty_list() {
        final List<Book> books = bookRepository.findAll();
        assertThat(books).isEmpty();
    }

    @Test
    void should_return_saved_book_when_save_book() {
        final Book book = Book.builder().title("new book").author("new author").year(2024).isbn("0000").build();
        final Book savedBook = bookRepository.save(book);
        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getId()).isPositive();
        assertThat(savedBook.getTitle()).isEqualTo(book.getTitle());
        assertThat(savedBook.getAuthor()).isEqualTo(book.getAuthor());
        assertThat(savedBook.getYear()).isEqualTo(book.getYear());
        assertThat(savedBook.getIsbn()).isEqualTo(book.getIsbn());
    }

}
