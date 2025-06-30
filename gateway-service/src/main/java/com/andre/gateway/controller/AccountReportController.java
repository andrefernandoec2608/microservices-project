package com.andre.gateway.controller;

import com.andre.gateway.dto.ClientReportDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/account_report")
@RequiredArgsConstructor
public class AccountReportController {

    private final WebClient clientServiceWebClient;
    private final WebClient accountServiceWebClient;

    @GetMapping("/{clientId}")
    public Mono<ResponseEntity<ClientReportDTO>> getClientReport(@PathVariable Long clientId) {
        Mono<ClientResponse> clientMono = clientServiceWebClient
                .get()
                .uri("/" + clientId)
                .retrieve()
                .bodyToMono(ClientResponse.class);

        Mono<List<AccountResponse>> accountsMono = accountServiceWebClient
                .get()
                .retrieve()
                .bodyToFlux(AccountResponse.class)
                .collectList();

        return Mono.zip(clientMono, accountsMono)
                .map(tuple -> {
                    ClientResponse client = tuple.getT1();
                    List<AccountResponse> accounts = tuple.getT2();
                    List<ClientReportDTO.AccountDTO> accountDTOs = accounts.stream()
                            .map(a -> new ClientReportDTO.AccountDTO(a.accountNumber, a.accountType, a.initialBalance, a.status))
                            .toList();

                    ClientReportDTO report = new ClientReportDTO(client.name, accountDTOs);
                    return ResponseEntity.ok(report);
                });
    }

    private record ClientResponse(Long clientId, String name) {}

    private record AccountResponse(String accountNumber, String accountType, BigDecimal initialBalance, Boolean status) {}
}