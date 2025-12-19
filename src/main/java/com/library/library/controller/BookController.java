package com.library.library.controller;

import com.library.library.dto.*;
import com.library.library.model.Book;
import com.library.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody CreateBookRequest request) {
        return ResponseEntity.ok(bookService.addBook(request.getAuthorIds(), request.getTitle()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBook(@PathVariable Long id) {
        Book book = bookService.getBook(id);
        return book != null ? ResponseEntity.ok(book) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody UpdateBookRequest request) {
        Book updated = bookService.updateBook(id, request.getAuthorIds(), request.getTitle());
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/borrow")
    public ResponseEntity<Book> borrowBook(@RequestBody BorrowRequest request) {
        return ResponseEntity.ok(bookService.borrowBook(request.getBookId(), request.getReaderId()));
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<Book> returnBook(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.returnBook(id));
    }

    @GetMapping("/author/{authorId}")
    public ResponseEntity<List<Book>> getBooksByAuthor(@PathVariable Long authorId) {
        return ResponseEntity.ok(bookService.getBooksByAuthor(authorId));
    }

    @GetMapping("/borrowed")
    public ResponseEntity<List<Book>> getBorrowedBooks() {
        return ResponseEntity.ok(bookService.findBorrowedBooks());
    }

    @GetMapping("/most-borrowed")
    public ResponseEntity<List<Book>> getMostBorrowedBooks(@RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(bookService.getMostBorrowedBooks(limit));
    }
}