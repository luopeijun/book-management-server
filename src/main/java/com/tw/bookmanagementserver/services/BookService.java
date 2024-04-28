package com.tw.bookmanagementserver.services;

import com.tw.bookmanagementserver.common.exception.BusinessException;
import com.tw.bookmanagementserver.common.exception.ErrorCode;
import com.tw.bookmanagementserver.models.Book;
import com.tw.bookmanagementserver.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND.getCode(), "Not found book id " + id));
    }

    public Book updateBookDetails(Long id, Book updatedBook) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND.getCode(), "Not found book id " + id));
        existingBook.setTitle(updatedBook.getTitle());
        existingBook.setAuthor(updatedBook.getAuthor());
        existingBook.setYear(updatedBook.getYear());
        existingBook.setIsbn(updatedBook.getIsbn());
        return bookRepository.save(existingBook);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}
