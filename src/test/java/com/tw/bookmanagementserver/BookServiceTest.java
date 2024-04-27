package com.tw.bookmanagementserver;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    void should_return_empty_book_list() {
        when(bookRepository.findAll()).thenReturn(emptyList());
        final List<Book> allBooks = bookService.getAllBooks();
        assertThat(allBooks).isEmpty();
        verify(bookRepository).findAll();
    }

    @Test
    void should_return_multiple_books() {
        final List<Book> books = List.of(
                Book.builder().title("book1").author("author1").year(2024).isbn("1234").build(),
                Book.builder().title("book2").author("author2").year(2024).isbn("5678").build()
        );

        when(bookRepository.findAll()).thenReturn(books);
        assertThat(bookService.getAllBooks()).isEqualTo(books);
        verify(bookRepository).findAll();
    }
}
