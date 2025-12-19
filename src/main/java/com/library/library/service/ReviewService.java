package com.library.library.service;

import com.library.library.model.Review;
import com.library.library.model.Book;
import com.library.library.model.Author;
import com.library.library.model.Reader;
import com.library.library.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private ReaderRepository readerRepository;

    @Transactional(readOnly = true)
    public List<Review> getReviewsByBook(Long bookId) {
        return reviewRepository.findByBookId(bookId);
    }

    @Transactional(readOnly = true)
    public List<Review> getReviewsByAuthor(Long authorId) {
        return reviewRepository.findByAuthorId(authorId);
    }

    @Transactional(readOnly = true)
    public List<Review> getReviewsByReader(Long readerId) {
        return reviewRepository.findByReaderId(readerId);
    }

    public Review addBookReview(Long readerId, Long bookId, int rating, String comment) {
        Reader reader = readerRepository.findById(readerId)
                .orElseThrow(() -> new RuntimeException("Reader not found"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        Review review = new Review();
        review.setReader(reader);
        review.setBook(book);
        review.setRating(rating);
        review.setComment(comment);

        Review savedReview = reviewRepository.save(review);

        // Обновляем средний рейтинг книги
        updateBookAverageRating(bookId);

        return savedReview;
    }

    public Review addAuthorReview(Long readerId, Long authorId, int rating, String comment) {
        Reader reader = readerRepository.findById(readerId)
                .orElseThrow(() -> new RuntimeException("Reader not found"));
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new RuntimeException("Author not found"));

        Review review = new Review();
        review.setReader(reader);
        review.setAuthor(author);
        review.setRating(rating);
        review.setComment(comment);

        Review savedReview = reviewRepository.save(review);

        // Обновляем средний рейтинг автора
        updateAuthorAverageRating(authorId);

        return savedReview;
    }

    private void updateBookAverageRating(Long bookId) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            Double avgRating = reviewRepository.findAverageRatingByBookId(bookId);
            Long reviewCount = reviewRepository.countByBookId(bookId);

            if (avgRating != null) {
                book.setAverageRating(avgRating);
                book.setReviewCount(reviewCount.intValue());
                bookRepository.save(book);
            }
        }
    }

    private void updateAuthorAverageRating(Long authorId) {
        Optional<Author> authorOptional = authorRepository.findById(authorId);
        if (authorOptional.isPresent()) {
            Author author = authorOptional.get();
            Double avgRating = reviewRepository.findAverageRatingByAuthorId(authorId);
            Long reviewCount = reviewRepository.countByAuthorId(authorId);

            if (avgRating != null) {
                author.setAverageRating(avgRating);
                author.setReviewCount(reviewCount.intValue());
                authorRepository.save(author);
            }
        }
    }

    @Transactional(readOnly = true)
    public List<Review> getTopReviews(int limit) {
        return reviewRepository.findAllByOrderByReviewDateDesc()
                .stream()
                .limit(limit)
                .collect(Collectors.toList());
    }

    public void verifyReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        review.setVerified(true);
        reviewRepository.save(review);
    }
}