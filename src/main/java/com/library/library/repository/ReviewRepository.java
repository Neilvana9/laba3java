package com.library.library.repository;

import com.library.library.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByBookId(Long bookId);
    List<Review> findByAuthorId(Long authorId);
    List<Review> findByReaderId(Long readerId);
    List<Review> findByVerified(boolean verified);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.book.id = :bookId")
    Double findAverageRatingByBookId(Long bookId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.author.id = :authorId")
    Double findAverageRatingByAuthorId(Long authorId);

    long countByBookId(Long bookId);
    long countByAuthorId(Long authorId);

    List<Review> findAllByOrderByReviewDateDesc();
}