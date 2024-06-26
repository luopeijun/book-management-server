package com.tw.bookmanagementserver;

import com.tw.bookmanagementserver.common.exception.BusinessException;
import com.tw.bookmanagementserver.models.Book;
import com.tw.bookmanagementserver.repositories.BookRepository;
import com.tw.bookmanagementserver.services.BookService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
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
                buildBook(),
                buildBook()
        );

        when(bookRepository.findAll()).thenReturn(books);
        assertThat(bookService.getAllBooks()).isEqualTo(books);
        verify(bookRepository).findAll();
    }

    @Test
    void should_return_created_book_when_create_book() {
        final Book book = buildBook();
        final Book savedBook = Book.builder().id(1L).title("new book").author("new author").year(2024).isbn("0000").build();
        when(bookRepository.save(book)).thenReturn(savedBook);
        final Book createdBook = bookService.createBook(book);
        assertThat(createdBook).isEqualTo(savedBook);
        verify(bookRepository).save(book);
    }

    @Test
    void should_return_book_when_id_exists() {
        final Book book = buildBook();
        when(bookRepository.findById(book.getId())).thenReturn(Optional.ofNullable(book));

        final Book foundBook = bookService.getBookById(book.getId());

        assertThat(foundBook).isEqualTo(book);
        verify(bookRepository).findById(book.getId());
    }

    @Test
    void should_throw_exceptions_when_id_not_exist() {
        when(bookRepository.findById(123L)).thenReturn(Optional.empty());
        BusinessException thrownException = assertThrows(BusinessException.class, () -> {
            bookService.getBookById(123L);
        });
        assertThat(thrownException.getMessage()).isEqualTo("Not found book id 123");
    }

    @Test
    void should_update_book_when_book_exists() {
        Book book = buildBook();
        Book updatedBook = Book.builder().id(book.getId()).title("update").author("update author").year(2000).isbn("1111").build();

        book.setTitle("update title");
        book.setAuthor("update author");
        book.setYear(2000);
        book.setIsbn("1111");

        when(bookRepository.findById(book.getId())).thenReturn(Optional.ofNullable(book));
        when(bookRepository.save(book)).thenReturn(updatedBook);
        Book result = bookService.updateBookDetails(book.getId(), updatedBook);

        assertThat(result).isNotNull();
        assertThat(updatedBook.getTitle()).isEqualTo(updatedBook.getTitle());
        assertThat(updatedBook.getAuthor()).isEqualTo(updatedBook.getAuthor());
        assertThat(updatedBook.getYear()).isEqualTo(updatedBook.getYear());
        assertThat(updatedBook.getIsbn()).isEqualTo(updatedBook.getIsbn());
    }

    @Test
    void should_delete_book() {
        final Book book = buildBook();
        bookService.deleteBook(book.getId());
        verify(bookRepository, times(1)).deleteById(book.getId());
    }

    private static Book buildBook() {
        return Book.builder().title("new book").author("new author").year(2024).isbn("0000").build();
    }

}
