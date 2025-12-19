package com.library.library.service;

import com.library.library.model.Reader;
import com.library.library.repository.ReaderRepository;
import com.library.library.repository.BookRepository;
import com.library.library.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ReaderService {
    @Autowired
    private ReaderRepository readerRepository;

    @Autowired
    private BookRepository bookRepository;

    public Reader addReader(String name) {
        if (readerRepository.existsByName(name)) {
            throw new RuntimeException("Reader with name " + name + " already exists");
        }
        Reader reader = new Reader();
        reader.setName(name);
        return readerRepository.save(reader);
    }

    public Reader getReader(Long id) {
        return readerRepository.findById(id).orElse(null);
    }

    public Reader updateReader(Long id, String name) {
        Reader reader = getReader(id);
        if (reader == null) {
            return null;
        }
        if (!reader.getName().equals(name) && readerRepository.existsByName(name)) {
            throw new RuntimeException("Reader with name " + name + " already exists");
        }
        reader.setName(name);
        return readerRepository.save(reader);
    }

    public void deleteReader(Long id) {
        readerRepository.deleteById(id);
    }

    public List<Reader> getAllReaders() {
        return readerRepository.findAll();
    }

    public List<Book> getBorrowedBooksByReader(Long readerId) {
        return bookRepository.findByCurrentReaderId(readerId); // Использует метод из BookRepository
    }
}