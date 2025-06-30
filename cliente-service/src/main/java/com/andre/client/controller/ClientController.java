package com.andre.client.controller;

import com.andre.client.model.Client;
import com.andre.client.producer.ClientEventProducer;
import com.andre.client.producer.EventMain;
import com.andre.client.service.ClientService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@Slf4j
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;
    private final ClientEventProducer clientEventProducer;

    @GetMapping
    public List<Client> getAllClients() {
        return clientService.getAllClients();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {
        return clientService.getClientById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Client createClient(@RequestBody Client client) throws JsonProcessingException {
        Client clientSaved = clientService.createClient(client);

        EventMain eventMain = new EventMain(clientSaved.getClientId());
        clientEventProducer.sendEventMainAsyn(eventMain);

        return clientSaved;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable Long id, @RequestBody Client client) {
        return clientService.updateClient(id, client)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }
}