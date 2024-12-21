package dette.boutique.services;

import java.util.ArrayList;
import java.util.List;

import dette.boutique.core.Item;
import dette.boutique.core.Service;
import dette.boutique.data.entities.Client;
import dette.boutique.data.entities.Dette;
import dette.boutique.data.repository.ClientRepository;

public class ClientService extends Service<Client> implements Item<Client> {
    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public void create(Client client) {
        clientRepository.insert(client);
    }

    @Override
    public void update(Client client) {
        clientRepository.insert(client);
    }

    // public boolean updateUserForClient(Client client) {
    //     clientRepository.updateUserForClient(client);
    //     return true;
    // }

    @Override
    public List<Client> list() {
        return clientRepository.selectAll();
    }

    public void affecterDetteAuClient(Client clientDette, Dette dette) {
        if (clientDette.getDettes() == null) {
            clientDette.setDettes(new ArrayList<>());
        }
        clientDette.getDettes().add(dette);

        update(clientDette);
    }

    public List<Client> listeClientsDispo() {
        return clientRepository.selectAll().stream()
                .filter(client -> client.getUser() == null)
                .toList();
    }

    public boolean numDispo(String tel) {
        return clientRepository.selectAll().stream()
                .filter(client -> client.getTelephone() == tel)
                .findFirst()
                .isPresent();
    }

    public Client findClientDette(Dette dette){
        return list().stream()
                .filter(client -> client.getId() == dette.getClient().getId())
                .findFirst()
                .orElse(null);

    }

    public Client findClientByTelephone(String telephone) {
        return clientRepository.selectAll().stream()
                .filter(client -> client.getTelephone().trim().compareTo(telephone.trim()) == 0)
                .findFirst()
                .orElse(null);
    }

    public Client findClientByUserId(int userId) {
        return clientRepository.selectAll().stream()
                .filter(client -> client.getUser().getId() == userId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Aucun client trouvé avec un utilisateur valide"));
    }
}
