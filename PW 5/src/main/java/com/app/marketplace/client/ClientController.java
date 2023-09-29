package com.app.marketplace.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/marketplace/client")
public class ClientController {
    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public ResponseEntity<List<Client>> getAllClients() {
        List<Client> clients = clientService.getAllClients();
        if (clients.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {
        return ResponseEntity.ofNullable(clientService.getClientById(id).orElse(null));
    }

    @PostMapping
    public ResponseEntity<Client> addClient(@RequestBody Client client) {
        return ResponseEntity.ok(clientService.saveClient(client));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Client> updatePhone(@PathVariable Long id, @RequestBody Client client) {
        return ResponseEntity.ofNullable(clientService.updateClient(id, client).orElse(null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteWashingMachine(@PathVariable Long id) {
        if (clientService.getClientById(id).isEmpty())
            return ResponseEntity.internalServerError().body("There is no such client");

        clientService.deleteClient(id);
        return ResponseEntity.ok().build();
    }
}

