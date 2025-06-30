package com.andre.account.controller;

import com.andre.account.model.Account;
import com.andre.account.model.AccountType;
import com.andre.account.model.Movement;
import com.andre.account.model.MovementType;
import com.andre.account.repository.AccountRepository;
import com.andre.account.repository.MovementRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private MovementRepository movementRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Account sampleAccount(Long id) {
        Account account = new Account();
        account.setId(id);
        account.setAccountNumber("12345678");
        account.setAccountType(AccountType.CURRENT);
        account.setInitialBalance(BigDecimal.valueOf(1000));
        account.setStatus(true);
        return account;
    }

    private Movement sampleMovement(Long id, Account account) {
        Movement movement = new Movement();
        movement.setId(id);
        movement.setDate(LocalDateTime.now());
        movement.setMovementType(MovementType.OPENING);
        movement.setAmount(BigDecimal.valueOf(500));
        movement.setBalance(BigDecimal.valueOf(1500));
        movement.setAccount(account);
        return movement;
    }

    @Test
    void testGetAllAccounts() throws Exception {
        Account acc1 = sampleAccount(1L);
        Account acc2 = sampleAccount(2L);

        Mockito.when(accountRepository.findAll())
                .thenReturn(Arrays.asList(acc1, acc2));

        mockMvc.perform(get("/api/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));
    }

    @Test
    void testGetAccountById_Found() throws Exception {
        Account account = sampleAccount(1L);

        Mockito.when(accountRepository.findById(1L))
                .thenReturn(Optional.of(account));

        mockMvc.perform(get("/api/accounts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.accountNumber").value("12345678"));
    }

    @Test
    void testGetAccountById_NotFound() throws Exception {
        Mockito.when(accountRepository.findById(99L))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/accounts/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetMovementsByAccount() throws Exception {
        Account account = sampleAccount(1L);
        Movement movement = sampleMovement(1L, account);

        Mockito.when(movementRepository.findByAccountId(1L))
                .thenReturn(Collections.singletonList(movement));

        mockMvc.perform(get("/api/accounts/1/movements"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].movementType").value("FIXED_TERM"))
                .andExpect(jsonPath("$[0].amount").value(500));
    }

    @Test
    void testCreateAccount_Success() throws Exception {
        Account requestAccount = new Account();
        requestAccount.setAccountNumber("99999999");
        requestAccount.setAccountType(AccountType.CURRENT);
        requestAccount.setInitialBalance(BigDecimal.valueOf(500));

        Account savedAccount = sampleAccount(1L);
        savedAccount.setMovements(Collections.emptyList());

        Mockito.when(accountRepository.save(any(Account.class)))
                .thenReturn(savedAccount);

        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestAccount)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.accountNumber").value("12345678"));
    }

    @Test
    void testCreateAccount_SavingsError() throws Exception {
        Account requestAccount = new Account();
        requestAccount.setAccountNumber("88888888");
        requestAccount.setAccountType(AccountType.SAVINGS);
        requestAccount.setInitialBalance(BigDecimal.valueOf(1000));

        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestAccount)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("SAVINGS accounts cannot be created manually."));
    }
}