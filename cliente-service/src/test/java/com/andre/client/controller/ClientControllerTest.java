package com.andre.client.controller;

import com.andre.client.model.Client;
import com.andre.client.producer.ClientEventProducer;
import com.andre.client.service.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClientController.class)
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientService clientService;

    @MockBean
    private ClientEventProducer clientEventProducer;

    @Autowired
    private ObjectMapper objectMapper;

    private Client createSampleClient(Long id) {
        Client client = new Client();
        client.setClientId(id);
        client.setName("Andre Ll");
        client.setGender("M");
        client.setAge(30);
        client.setIdentification("1234567890");
        client.setAddress("Gonzales Suarez");
        client.setPhone("0987654321");
        client.setPassword("securePassword");
        client.setStatus(true);
        return client;
    }

    @Test
    void testGetAllClients() throws Exception {
        Client client1 = createSampleClient(1L);
        Client client2 = createSampleClient(2L);

        Mockito.when(clientService.getAllClients()).thenReturn(Arrays.asList(client1, client2));

        mockMvc.perform(get("/api/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].clientId").value(1L))
                .andExpect(jsonPath("$[0].name").value("Andre Ll"))
                .andExpect(jsonPath("$[1].clientId").value(2L));
    }

    @Test
    void testGetClientById_Found() throws Exception {
        Client client = createSampleClient(1L);

        Mockito.when(clientService.getClientById(1L)).thenReturn(Optional.of(client));

        mockMvc.perform(get("/api/clients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientId").value(1L))
                .andExpect(jsonPath("$.name").value("Andre Ll"))
                .andExpect(jsonPath("$.gender").value("M"))
                .andExpect(jsonPath("$.age").value(30))
                .andExpect(jsonPath("$.identification").value("1234567890"))
                .andExpect(jsonPath("$.address").value("Gonzales Suarez"))
                .andExpect(jsonPath("$.phone").value("0987654321"))
                .andExpect(jsonPath("$.password").value("securePassword"))
                .andExpect(jsonPath("$.status").value(true));
    }

    @Test
    void testGetClientById_NotFound() throws Exception {
        Mockito.when(clientService.getClientById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/clients/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateClient() throws Exception {
        Client requestClient = new Client();
        requestClient.setClientId(null);
        requestClient.setName("Andre Ll");
        requestClient.setGender("M");
        requestClient.setAge(30);
        requestClient.setIdentification("1234567890");
        requestClient.setAddress("Gonzales Suarez 2");
        requestClient.setPhone("0987654321");
        requestClient.setPassword("securePassword");
        requestClient.setStatus(true);

        Client savedClient = createSampleClient(1L);

        Mockito.when(clientService.createClient(any(Client.class))).thenReturn(savedClient);
        Mockito.doNothing().when(clientEventProducer).sendEventMainAsyn(any());

        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestClient)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientId").value(1L))
                .andExpect(jsonPath("$.name").value("Andre Ll"));
    }


    @Test
    void testUpdateClient_Found() throws Exception {
        Client requestClient = new Client();
        requestClient.setClientId(null);
        requestClient.setName("UPDATED Andre Ll");
        requestClient.setGender("F");
        requestClient.setAge(28);
        requestClient.setIdentification("9876543210");
        requestClient.setAddress("UPDATED Gonzales Suarez 2");
        requestClient.setPhone("0123456789");
        requestClient.setPassword("newPassword");
        requestClient.setStatus(false);

        Client updatedClient = new Client();
        updatedClient.setClientId(1L);
        updatedClient.setName("UPDATED Andre Ll");
        updatedClient.setGender("F");
        updatedClient.setAge(28);
        updatedClient.setIdentification("9876543210");
        updatedClient.setAddress("UPDATED Gonzales Suarez 2");
        updatedClient.setPhone("0123456789");
        updatedClient.setPassword("newPassword");
        updatedClient.setStatus(false);

        Mockito.when(clientService.updateClient(eq(1L), any(Client.class)))
                .thenReturn(Optional.of(updatedClient));

        mockMvc.perform(put("/api/clients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestClient)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientId").value(1L))
                .andExpect(jsonPath("$.name").value("UPDATED Andre Ll"))
                .andExpect(jsonPath("$.gender").value("F"))
                .andExpect(jsonPath("$.age").value(28))
                .andExpect(jsonPath("$.identification").value("9876543210"))
                .andExpect(jsonPath("$.address").value("UPDATED Gonzales Suarez 2"))
                .andExpect(jsonPath("$.phone").value("0123456789"))
                .andExpect(jsonPath("$.password").value("newPassword"))
                .andExpect(jsonPath("$.status").value(false));
    }

    @Test
    void testUpdateClient_NotFound() throws Exception {
        Mockito.when(clientService.updateClient(eq(99L), any(Client.class)))
                .thenReturn(Optional.empty());

        mockMvc.perform(put("/api/clients/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new Client())))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteClient() throws Exception {
        Mockito.doNothing().when(clientService).deleteClient(1L);

        mockMvc.perform(delete("/api/clients/1"))
                .andExpect(status().isNoContent());
    }
}