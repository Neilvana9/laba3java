package com.library.library.service;

import com.library.library.model.Fine;
import com.library.library.repository.FineRepository;
import com.library.library.repository.ReaderRepository;
import com.library.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class FineService {
    @Autowired
    private FineRepository fineRepository;

    @Autowired
    private ReaderRepository readerRepository;

    @Autowired
    private BookRepository bookRepository;

    @Transactional(readOnly = true)
    public Fine getFine(Long id) {
        return fineRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Fine> getAllFines() {
        return fineRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Fine> getFinesByReader(Long readerId) {
        return fineRepository.findByReaderId(readerId);
    }

    @Transactional(readOnly = true)
    public List<Fine> getUnpaidFinesByReader(Long readerId) {
        return fineRepository.findByReaderIdAndPaidFalse(readerId);
    }

    public Fine addFine(Long readerId, Long bookId, double amount) {
        Fine fine = new Fine();
        fine.setReader(readerRepository.findById(readerId)
                .orElseThrow(() -> new RuntimeException("Reader not found")));
        fine.setBook(bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found")));
        fine.setAmount(amount);
        return fineRepository.save(fine);
    }

    public Fine updateFine(Long id, Long readerId, Long bookId, double amount) {
        Fine fine = getFine(id);
        if (fine == null) {
            return null;
        }
        fine.setReader(readerRepository.findById(readerId)
                .orElseThrow(() -> new RuntimeException("Reader not found")));
        fine.setBook(bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found")));
        fine.setAmount(amount);
        return fineRepository.save(fine);
    }

    public void deleteFine(Long id) {
        fineRepository.deleteById(id);
    }

    public Fine markFineAsPaid(Long id) {
        Fine fine = getFine(id);
        if (fine == null) {
            throw new RuntimeException("Fine not found");
        }
        fine.setPaid(true);
        return fineRepository.save(fine);
    }

    public List<Fine> payAllFinesForReader(Long readerId) {
        List<Fine> unpaidFines = fineRepository.findByReaderIdAndPaidFalse(readerId);
        for (Fine fine : unpaidFines) {
            fine.setPaid(true);
        }
        return fineRepository.saveAll(unpaidFines);
    }
}