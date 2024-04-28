package com.tw.bookmanagementserver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
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
    void should_return_all_books() {
        final Book book = buildBook();
        entityManager.persist(book);
        final List<Book> allBooks = bookRepository.findAll();
        assertThat(allBooks)
                .hasSize(1)
                .containsOnly(book);
    }


    @Test
    void should_return_saved_book_when_save_book() {
        final Book book = buildBook();
        final Book savedBook = bookRepository.save(book);
        assertThat(savedBook.getId()).isNotNull();
        assertThat(savedBook.getId()).isPositive();
        assertThat(savedBook.getTitle()).isEqualTo(book.getTitle());
        assertThat(savedBook.getAuthor()).isEqualTo(book.getAuthor());
        assertThat(savedBook.getYear()).isEqualTo(book.getYear());
        assertThat(savedBook.getIsbn()).isEqualTo(book.getIsbn());
    }

    @Test
    void should_return_book_when_book_exists() {
        final Book book = buildBook();
        entityManager.persist(book);

        final Optional<Book> foundBook = bookRepository.findById(book.getId());
        assertTrue(foundBook.isPresent());
        assertEquals(book, foundBook.get());
    }

    @Test
    void should_throw_exceptions_when_id_not_exist() {
        final Optional<Book> foundBook = bookRepository.findById(123L);
        assertFalse(foundBook.isPresent());
    }

    @Test
    void should_update_book_when_book_exists() {
        Book book = buildBook();
        entityManager.persist(book);

        book.setTitle("update title");
        book.setAuthor("update author");
        book.setYear(2000);
        book.setIsbn("1111");

        Book updatedBook = bookRepository.save(book);

        assertThat(updatedBook.getTitle()).isEqualTo("update title");
        assertThat(updatedBook.getAuthor()).isEqualTo("update author");
        assertThat(updatedBook.getYear()).isEqualTo(2000);
        assertThat(updatedBook.getIsbn()).isEqualTo("1111");
    }

    @Test
    void should_delete_book_success() {
        final Book book = buildBook();
        bookRepository.save(book);
        bookRepository.deleteById(book.getId());
        assertThat(bookRepository.findById(book.getId())).isEmpty();
    }

    @Test
    void should_delete_book_when_id_not_exist() {
        Long nonExistingBookId = 123L;
        assertThrows(EmptyResultDataAccessException.class, () -> {
            bookRepository.deleteById(nonExistingBookId);
        });
    }

    private static Book buildBook() {
        return Book.builder().title("new book").author("new author").year(2024).isbn("0000").build();
    }

}
