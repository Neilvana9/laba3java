package com.library.library.controller;

import com.library.library.model.Review;
import com.library.library.repository.ReviewRepository;
import com.library.library.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ReviewRepository reviewRepository; // Добавьте эту строку

    @PostMapping("/book")
    public ResponseEntity<Review> addBookReview(
            @RequestParam Long readerId,
            @RequestParam Long bookId,
            @RequestParam int rating,
            @RequestParam(required = false) String comment) {
        return ResponseEntity.ok(reviewService.addBookReview(readerId, bookId, rating, comment));
    }

    @PostMapping("/author")
    public ResponseEntity<Review> addAuthorReview(
            @RequestParam Long readerId,
            @RequestParam Long authorId,
            @RequestParam int rating,
            @RequestParam(required = false) String comment) {
        return ResponseEntity.ok(reviewService.addAuthorReview(readerId, authorId, rating, comment));
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<Review>> getReviewsByBook(@PathVariable Long bookId) {
        return ResponseEntity.ok(reviewService.getReviewsByBook(bookId));
    }

    @GetMapping("/author/{authorId}")
    public ResponseEntity<List<Review>> getReviewsByAuthor(@PathVariable Long authorId) {
        return ResponseEntity.ok(reviewService.getReviewsByAuthor(authorId));
    }

    @GetMapping("/reader/{readerId}")
    public ResponseEntity<List<Review>> getReviewsByReader(@PathVariable Long readerId) {
        return ResponseEntity.ok(reviewService.getReviewsByReader(readerId));
    }

    @GetMapping("/top")
    public ResponseEntity<List<Review>> getTopReviews(@RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(reviewService.getTopReviews(limit));
    }

    @PostMapping("/{reviewId}/verify")
    public ResponseEntity<Review> verifyReview(@PathVariable Long reviewId) {
        reviewService.verifyReview(reviewId);
        Review review = reviewRepository.findById(reviewId) // Теперь reviewRepository доступен
                .orElseThrow(() -> new RuntimeException("Review not found"));
        return ResponseEntity.ok(review);
    }
}