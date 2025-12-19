package com.library.library.service;

import com.library.library.model.Author;
import com.library.library.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AuthorService {
    @Autowired
    private AuthorRepository authorRepository;

    public Author addAuthor(String name) {
        if (authorRepository.existsByName(name)) {
            throw new RuntimeException("Author with name " + name + " already exists");
        }
        Author author = new Author();
        author.setName(name);
        return authorRepository.save(author);
    }

    public Author getAuthor(Long id) {
        return authorRepository.findById(id).orElse(null);
    }

    public Author updateAuthor(Long id, String name) {
        Author author = getAuthor(id);
        if (author == null) {
            return null;
        }
        if (!author.getName().equals(name) && authorRepository.existsByName(name)) {
            throw new RuntimeException("Author with name " + name + " already exists");
        }
        author.setName(name);
        return authorRepository.save(author);
    }

    public void deleteAuthor(Long id) {
        authorRepository.deleteById(id);
    }

    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }
}