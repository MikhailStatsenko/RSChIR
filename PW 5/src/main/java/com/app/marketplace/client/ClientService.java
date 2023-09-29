package com.app.marketplace.client;

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
        return (List<Client>) clientRepository.findAll();
    }

    public Optional<Client> getClientById(Long id) {
        return clientRepository.findById(id);
    }

    public Client saveClient(Client client) {
        return clientRepository.save(client);
    }

    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }

    public Optional<Client> updateClient(Long id, Client updatedClient) {
        Optional<Client> clientOptional = getClientById(id);
        if (clientOptional.isEmpty())
            return Optional.empty();

        Client dbClient = clientOptional.get();

        if (updatedClient.getName() != null && !updatedClient.getName().isBlank())
            dbClient.setName(updatedClient.getName());

        if (updatedClient.getEmail() != null && !updatedClient.getEmail().isBlank())
            dbClient.setEmail(updatedClient.getEmail());

        if (updatedClient.getLogin() != null && !updatedClient.getLogin().isBlank())
            dbClient.setLogin(updatedClient.getLogin());

        if (updatedClient.getPassword() != null && !updatedClient.getPassword().isBlank())
            dbClient.setPassword(updatedClient.getPassword());

        return Optional.of(saveClient(dbClient));
    }

}

