package com.example.library.controller;

import com.example.library.dao.LoanDao;
import com.example.library.model.Loan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loans")
public class LoanController {

    private final LoanDao loanDao;

    public LoanController(LoanDao loanDao) {
        this.loanDao = loanDao;
    }

    @GetMapping
    public List<Loan> listLoans() {
        return loanDao.getAllLoans();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Loan> getLoan(@PathVariable Long id) {
        return loanDao.getLoanById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Loan> addLoan(@RequestBody Loan loan) {
        Loan saved = loanDao.addLoan(loan);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Loan> updateLoan(@PathVariable Long id, @RequestBody Loan loan) {
        return loanDao.getLoanById(id)
                .map(existing -> {
                    existing.setBook(loan.getBook());
                    existing.setMember(loan.getMember());
                    existing.setLoanDate(loan.getLoanDate());
                    existing.setDueDate(loan.getDueDate());
                    return ResponseEntity.ok(loanDao.updateLoan(existing));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLoan(@PathVariable Long id) {
        loanDao.deleteLoan(id);
        return ResponseEntity.noContent().build();
    }
}
