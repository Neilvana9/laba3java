package com.library.library.repository;

import com.library.library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByAuthorsId(Long authorId); // Добавьте этот метод

    List<Book> findByCurrentReaderId(Long readerId); // Добавьте этот метод

    @Query("SELECT b FROM Book b WHERE b.borrowed = true")
    List<Book> findBorrowedBooks();

    @Query("SELECT b FROM Book b ORDER BY b.borrowCount DESC")
    List<Book> findTopByOrderByBorrowCountDesc(int limit);

    @Query("SELECT b FROM Book b ORDER BY b.averageRating DESC")
    List<Book> findTopByOrderByAverageRatingDesc(int limit);

    List<Book> findAllByBorrowedFalse();
}