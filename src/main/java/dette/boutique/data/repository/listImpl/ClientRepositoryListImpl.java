package dette.boutique.data.repository.listImpl;

import java.util.ArrayList;
import java.util.List;

import dette.boutique.core.repository.impl.RepositoryListImpl;
import dette.boutique.data.entities.Client;
import dette.boutique.data.repository.ClientRepository;

public class ClientRepositoryListImpl extends RepositoryListImpl<Client> implements ClientRepository {

    public ClientRepositoryListImpl(List<Client> data) {
        super(data);
    }

    public ClientRepositoryListImpl() {
        // throw new UnsupportedOperationException("Not supported yet.");
        super(new ArrayList<>());
    }
    @Override
    public Client findByTel(String telephone) {
        return data.stream()
                .filter(client -> client.getTelephone().compareTo(telephone) == 0)
                .findFirst()
                .orElse(null);
    }

    @Override
    public Client selectById(int id) {
        return null;
        // return data.stream()
        // .filter(client -> client.getId()==(id) == 0)
        // .findFirst()
        // .orElse(null);
    }

    @Override
    public void remove(Client element) {
        
    }

}
