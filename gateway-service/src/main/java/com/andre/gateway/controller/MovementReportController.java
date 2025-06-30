package com.andre.gateway.controller;

import com.andre.gateway.dto.MovementReportDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/movement_report")
@RequiredArgsConstructor
public class MovementReportController {

    private final WebClient accountServiceWebClient;
    private final WebClient clientServiceWebClient;

    @GetMapping("/{accountNumber}")
    public Mono<ResponseEntity<MovementReportDTO>> getMovementReport(@PathVariable String accountNumber) {
        Mono<AccountResponse> accountMono = accountServiceWebClient
                .get()
                .uri("/{accountNumber}", accountNumber)
                .retrieve()
                .bodyToMono(AccountResponse.class);

        return accountMono.flatMap(account -> {
            Mono<List<MovementResponse>> movementsMono = accountServiceWebClient
                    .get()
                    .uri("/{accountId}/movements", account.id())
                    .retrieve()
                    .bodyToFlux(MovementResponse.class)
                    .collectList();

            Mono<ClientResponse> clientMono = clientServiceWebClient
                    .get()
                    .uri("/{clientId}", account.clientId())
                    .retrieve()
                    .bodyToMono(ClientResponse.class);

            return Mono.zip(clientMono, movementsMono)
                    .map(tuple -> {
                        ClientResponse client = tuple.getT1();
                        List<MovementResponse> movements = tuple.getT2();

                        movements.sort(Comparator.comparing(MovementResponse::date).reversed());

                        List<MovementReportDTO.MovementInfo> movementInfos = movements.stream()
                                .map(m -> new MovementReportDTO.MovementInfo(
                                        m.date(),
                                        m.movementType(),
                                        m.amount(),
                                        m.balance()
                                ))
                                .toList();

                        MovementReportDTO report = new MovementReportDTO(
                                client.name(),
                                account.accountType(),
                                movementInfos
                        );

                        return ResponseEntity.ok(report);
                    });
        });
    }

    public record AccountResponse(
            Long id,
            String accountNumber,
            String accountType,
            Long clientId
    ) {}

    public record MovementResponse(
            Long id,
            String date,
            String movementType,
            Double amount,
            Double balance
    ) {}

    public record ClientResponse(
            Long clientId,
            String name
    ) {}
}