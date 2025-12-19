package com.library.library.controller;

import com.library.library.dto.CreateFineRequest;
import com.library.library.dto.UpdateFineRequest;
import com.library.library.model.Fine;
import com.library.library.service.FineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fines")
public class FineController {

    @Autowired
    private FineService fineService;

    @PostMapping
    public ResponseEntity<Fine> addFine(@RequestBody CreateFineRequest request) {
        return ResponseEntity.ok(fineService.addFine(request.getReaderId(), request.getBookId(), request.getAmount()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Fine> getFine(@PathVariable Long id) {
        Fine fine = fineService.getFine(id);
        return fine != null ? ResponseEntity.ok(fine) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<Fine>> getAllFines() {
        return ResponseEntity.ok(fineService.getAllFines());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Fine> updateFine(@PathVariable Long id, @RequestBody UpdateFineRequest request) {
        Fine updated = fineService.updateFine(id, request.getReaderId(), request.getBookId(), request.getAmount());
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFine(@PathVariable Long id) {
        fineService.deleteFine(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/pay")
    public ResponseEntity<Fine> markFineAsPaid(@PathVariable Long id) {
        return ResponseEntity.ok(fineService.markFineAsPaid(id));
    }

    @GetMapping("/reader/{readerId}")
    public ResponseEntity<List<Fine>> getFinesByReader(@PathVariable Long readerId) {
        return ResponseEntity.ok(fineService.getFinesByReader(readerId));
    }

    @GetMapping("/reader/{readerId}/unpaid")
    public ResponseEntity<List<Fine>> getUnpaidFinesByReader(@PathVariable Long readerId) {
        return ResponseEntity.ok(fineService.getUnpaidFinesByReader(readerId));
    }

    @PostMapping("/reader/{readerId}/pay-all")
    public ResponseEntity<List<Fine>> payAllFinesForReader(@PathVariable Long readerId) {
        return ResponseEntity.ok(fineService.payAllFinesForReader(readerId));
    }
}