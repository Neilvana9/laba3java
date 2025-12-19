package com.library.library.service;

import com.library.library.model.Book;
import com.library.library.model.Reader;
import com.library.library.model.Author;
import com.library.library.model.Fine;
import com.library.library.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BookService {
    private static final double FINE_RATE_PER_DAY = 100.0;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private ReaderRepository readerRepository;

    @Autowired
    private FineRepository fineRepository;

    @Transactional(readOnly = true)
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Book getBook(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    public Book addBook(List<Long> authorIds, String title) {
        Book book = new Book();
        book.setTitle(title);

        List<Author> authors = authorRepository.findAllById(authorIds);
        if (authors.size() != authorIds.size()) {
            throw new RuntimeException("One or more authors not found");
        }

        book.setAuthors(authors);
        return bookRepository.save(book);
    }

    public Book updateBook(Long id, List<Long> authorIds, String title) {
        Book book = getBook(id);
        if (book == null) {
            return null;
        }

        List<Author> authors = authorRepository.findAllById(authorIds);
        if (authors.size() != authorIds.size()) {
            throw new RuntimeException("One or more authors not found");
        }

        book.setAuthors(authors);
        book.setTitle(title);
        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    public Book borrowBook(Long bookId, Long readerId) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        Optional<Reader> readerOptional = readerRepository.findById(readerId);

        if (bookOptional.isEmpty() || readerOptional.isEmpty()) {
            throw new RuntimeException("Book or reader not found");
        }

        Book book = bookOptional.get();
        Reader reader = readerOptional.get();

        if (book.isBorrowed()) {
            throw new RuntimeException("Book is already borrowed");
        }

        book.setBorrowed(true);
        book.setCurrentReader(reader);
        book.setBorrowDate(LocalDate.now());
        book.setDueDate(LocalDate.now().plusDays(30));
        book.setBorrowCount(book.getBorrowCount() + 1);

        return bookRepository.save(book);
    }

    public Book returnBook(Long bookId) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        if (bookOptional.isEmpty()) {
            throw new RuntimeException("Book not found");
        }

        Book book = bookOptional.get();
        if (!book.isBorrowed()) {
            throw new RuntimeException("Book is not currently borrowed");
        }

        LocalDate today = LocalDate.now();
        if (today.isAfter(book.getDueDate())) {
            long daysOverdue = ChronoUnit.DAYS.between(book.getDueDate(), today);
            double fineAmount = daysOverdue * FINE_RATE_PER_DAY;

            Fine fine = new Fine();
            fine.setReader(book.getCurrentReader());
            fine.setBook(book);
            fine.setAmount(fineAmount);
            fine.setPaid(false);

            fineRepository.save(fine);
        }

        book.setBorrowed(false);
        book.setCurrentReader(null);
        book.setBorrowDate(null);
        book.setDueDate(null);

        return bookRepository.save(book);
    }

    @Transactional(readOnly = true)
    public List<Book> getBooksByAuthor(Long authorId) {
        return bookRepository.findByAuthorsId(authorId);
    }

    @Transactional(readOnly = true)
    public List<Book> findBorrowedBooks() {
        return bookRepository.findBorrowedBooks();
    }

    @Transactional(readOnly = true)
    public List<Book> getMostBorrowedBooks(int limit) {
        return bookRepository.findTopByOrderByBorrowCountDesc(limit);
    }
}