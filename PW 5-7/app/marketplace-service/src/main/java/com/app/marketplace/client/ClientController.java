package com.app.marketplace.client;

import com.app.marketplace.auth.RequiresRole;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/marketplace/client")
public class ClientController {
    private final Gson gson;
    private final ClientService clientService;

    @Autowired
    public ClientController(Gson gson, ClientService clientService) {
        this.gson = gson;
        this.clientService = clientService;
    }

    @GetMapping
    @RequiresRole("ADMINISTRATOR")
    public ResponseEntity<?> getAllClients() {
        List<Client> clients = clientService.getAllClients();
        if (clients.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(gson.toJson(clients));
    }

    @GetMapping("/{id}")
    @RequiresRole({"USER", "ADMINISTRATOR"})
    public ResponseEntity<?> getClientById(@PathVariable Long id) {
        Optional<Client> clientOptional = clientService.getClientById(id);
        if (clientOptional.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok().body(gson.toJson(clientOptional.get()));
    }

    @PostMapping
    @RequiresRole({"USER", "ADMINISTRATOR"})
    public ResponseEntity<?> addClient(@RequestBody Client client) {
        return ResponseEntity.ok(gson.toJson(clientService.saveClient(client)));
    }

    @PutMapping("/{id}")
    @RequiresRole({"USER", "ADMINISTRATOR"})
    public ResponseEntity<?> updateClient(@PathVariable Long id, @RequestBody Client client) {
        Optional<Client> clientOptional = clientService.updateClient(id, client);
        if (clientOptional.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok().body(gson.toJson(clientOptional.get()));
    }

    @DeleteMapping("/{id}")
    @RequiresRole("ADMINISTRATOR")
    public ResponseEntity<String> deleteClient(@PathVariable Long id) {
        if (clientService.getClientById(id).isEmpty())
            return ResponseEntity.internalServerError().body("There is no such client");

        clientService.deleteClient(id);
        return ResponseEntity.ok().build();
    }
}

