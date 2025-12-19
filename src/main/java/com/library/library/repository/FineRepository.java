package com.library.library.repository;

import com.library.library.model.Fine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FineRepository extends JpaRepository<Fine, Long> {
    List<Fine> findByReaderId(Long readerId);
    List<Fine> findByBookId(Long bookId);
    List<Fine> findByPaid(boolean paid);
    List<Fine> findByReaderIdAndPaid(Long readerId, boolean paid);
    List<Fine> findByReaderIdAndPaidFalse(Long readerId);
}