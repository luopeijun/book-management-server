package com.tw.bookmanagementserver.repositories;

import com.tw.bookmanagementserver.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
}
