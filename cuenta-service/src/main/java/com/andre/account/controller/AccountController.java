package com.andre.account.controller;

import com.andre.account.model.Account;
import com.andre.account.model.AccountType;
import com.andre.account.model.Movement;
import com.andre.account.repository.AccountRepository;
import com.andre.account.repository.MovementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountRepository accountRepository;
    private final MovementRepository movementRepository;

    @Autowired
    public AccountController(AccountRepository accountRepository, MovementRepository movementRepository) {
        this.accountRepository = accountRepository;
        this.movementRepository = movementRepository;
    }

    @GetMapping
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable Long id) {
        return accountRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{accountId}/movements")
    public List<Movement> getMovementsByAccount(@PathVariable Long accountId) {
        return movementRepository.findByAccountId(accountId);
    }

    @PostMapping
    public ResponseEntity<?> createAccount(@RequestBody Account account) {
        if (account.getAccountType().equals(AccountType.SAVINGS)) {
            return ResponseEntity.badRequest().body("SAVINGS accounts cannot be created manually.");
        }

        account.setInitialBalance(account.getInitialBalance() != null ? account.getInitialBalance() : BigDecimal.ZERO);
        account.setStatus(true);

        Movement initialMovement = new Movement();
        initialMovement.setDate(LocalDateTime.now());
        initialMovement.setMovementType("OPENING");
        initialMovement.setAmount(BigDecimal.ZERO);
        initialMovement.setBalance(account.getInitialBalance());
        initialMovement.setAccount(account);

        account.setMovements(Collections.singletonList(initialMovement));

        Account savedAccount = accountRepository.save(account);
        return ResponseEntity.ok(savedAccount);
    }
}