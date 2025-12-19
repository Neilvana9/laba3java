package com.library.library.controller;

import com.library.library.dto.CreateReaderRequest;
import com.library.library.dto.UpdateReaderRequest;
import com.library.library.model.Book;
import com.library.library.model.Reader;
import com.library.library.service.ReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/readers")
public class ReaderController {

    @Autowired
    private ReaderService readerService;

    @PostMapping
    public ResponseEntity<Reader> addReader(@RequestBody CreateReaderRequest request) {
        return ResponseEntity.ok(readerService.addReader(request.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reader> getReader(@PathVariable Long id) {
        Reader reader = readerService.getReader(id);
        return reader != null ? ResponseEntity.ok(reader) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<Reader>> getAllReaders() {
        return ResponseEntity.ok(readerService.getAllReaders());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reader> updateReader(@PathVariable Long id, @RequestBody UpdateReaderRequest request) {
        Reader updated = readerService.updateReader(id, request.getName());
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReader(@PathVariable Long id) {
        readerService.deleteReader(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/borrowed-books")
    public ResponseEntity<List<Book>> getBorrowedBooks(@PathVariable Long id) {
        return ResponseEntity.ok(readerService.getBorrowedBooksByReader(id));
    }
}