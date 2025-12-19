package com.library.library.service;

import com.library.library.model.Book;
import com.library.library.model.Reader;
import com.library.library.repository.BookRepository;
import com.library.library.repository.ReaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ReaderRepository readerRepository;

    // Топ читателей по количеству взятых книг
    public List<Reader> getTopReaders(int limit) {
        return readerRepository.findAll().stream()
                .sorted((r1, r2) -> Integer.compare(r2.getBorrowedBooks().size(), r1.getBorrowedBooks().size()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    // Средняя продолжительность чтения книг
    public double getAverageReadingDuration() {
        List<Book> books = bookRepository.findAllByBorrowedFalse(); // Только возвращенные книги
        long totalDays = 0;
        int count = 0;

        for (Book book : books) {
            if (book.getBorrowDate() != null && book.getDueDate() != null) {
                long days = ChronoUnit.DAYS.between(book.getBorrowDate(), book.getDueDate());
                totalDays += days;
                count++;
            }
        }

        return count > 0 ? (double) totalDays / count : 0;
    }

    // Процент возврата книг вовремя
    public double getOnTimeReturnPercentage() {
        List<Book> books = bookRepository.findAllByBorrowedFalse(); // Только возвращенные книги
        long onTimeReturns = 0;
        long totalReturns = 0;

        for (Book book : books) {
            if (book.getBorrowDate() != null) {
                totalReturns++;
                if (book.getDueDate() != null && !LocalDate.now().isAfter(book.getDueDate())) {
                    onTimeReturns++;
                }
            }
        }

        return totalReturns > 0 ? (double) onTimeReturns / totalReturns * 100 : 0;
    }

    // Рекомендации книг на основе рейтингов
    public List<Book> getHighlyRatedBooks(int limit) {
        return bookRepository.findTopByOrderByAverageRatingDesc(limit);
    }
}