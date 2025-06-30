package com.andre.client.service;

import com.andre.client.model.Client;
import com.andre.client.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Optional<Client> getClientById(Long id) {
        return clientRepository.findById(id);
    }

    public Client createClient(Client client) {
        return clientRepository.save(client);
    }

    public Optional<Client> updateClient(Long id, Client updatedClient) {
        return clientRepository.findById(id).map(existingClient -> {
            existingClient.setName(updatedClient.getName());
            existingClient.setGender(updatedClient.getGender());
            existingClient.setAge(updatedClient.getAge());
            existingClient.setIdentification(updatedClient.getIdentification());
            existingClient.setAddress(updatedClient.getAddress());
            existingClient.setPhone(updatedClient.getPhone());
            existingClient.setPassword(updatedClient.getPassword());
            existingClient.setStatus(updatedClient.getStatus());
            return clientRepository.save(existingClient);
        });
    }

    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }
}