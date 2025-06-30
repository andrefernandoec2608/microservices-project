package com.andre.account.controller;

import com.andre.account.exception.SavingsAccountManualCreationException;
import com.andre.account.model.Account;
import com.andre.account.model.AccountType;
import com.andre.account.model.Movement;
import com.andre.account.model.MovementType;
import com.andre.account.repository.AccountRepository;
import com.andre.account.repository.MovementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountRepository accountRepository;
    private final MovementRepository movementRepository;

    @GetMapping
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<Account> getAccountById(@PathVariable String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
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
            throw new SavingsAccountManualCreationException();
        }

        account.setInitialBalance(BigDecimal.ZERO);
        account.setStatus(true);

        Movement initialMovement = new Movement();
        initialMovement.setDate(LocalDateTime.now());
        initialMovement.setMovementType(MovementType.OPENING);
        initialMovement.setAmount(BigDecimal.ZERO);
        initialMovement.setBalance(BigDecimal.ZERO);
        initialMovement.setAccount(account);

        account.setMovements(Collections.singletonList(initialMovement));

        Account savedAccount = accountRepository.save(account);
        return ResponseEntity.ok(savedAccount);
    }

    @PostMapping("/number/{accountNumber}/movements")
    public ResponseEntity<?> createMovementByAccountNumber(@PathVariable String accountNumber, @RequestBody Movement movement) {
        return accountRepository.findByAccountNumber(accountNumber)
                .map(account -> {
                    movement.setDate(LocalDateTime.now());
                    movement.setAccount(account);

                    BigDecimal lastBalance = account.getInitialBalance();
                    BigDecimal newBalance = lastBalance.add(movement.getAmount());

                    movement.setBalance(newBalance);

                    // Update account initialBalance with new balance
                    account.setInitialBalance(newBalance);
                    accountRepository.save(account);

                    Movement savedMovement = movementRepository.save(movement);
                    return ResponseEntity.ok(savedMovement);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}