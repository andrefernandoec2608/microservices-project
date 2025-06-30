package com.andre.account.service;

import com.andre.account.model.Account;
import com.andre.account.model.AccountType;
import com.andre.account.model.Movement;
import com.andre.account.model.MovementType;
import com.andre.account.repository.AccountRepository;
import com.andre.account.util.AccountNumberGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConsumerEventService {

    private final ObjectMapper objectMapper;
    private final AccountRepository accountRepository;

    public void processMainEvent(ConsumerRecord<Integer, String> consumerRecord) throws JsonProcessingException  {
        EventMain eventMainDTO = objectMapper.readValue(consumerRecord.value(), EventMain.class);

        Account account = new Account();
        account.setAccountNumber(AccountNumberGenerator.generateAccountNumber());
        account.setAccountType(AccountType.SAVINGS); // SAVINGS is by DEFAULT.
        account.setInitialBalance(BigDecimal.ZERO);
        account.setStatus(true);
        account.setClientId(eventMainDTO.clientId());

        Movement initialMovement = new Movement();
        initialMovement.setDate(LocalDateTime.now());
        initialMovement.setMovementType(MovementType.OPENING);
        initialMovement.setAmount(BigDecimal.ZERO);
        initialMovement.setBalance(BigDecimal.ZERO);
        initialMovement.setAccount(account);

        account.setMovements(Collections.singletonList(initialMovement));

        accountRepository.save(account);
    }
}
