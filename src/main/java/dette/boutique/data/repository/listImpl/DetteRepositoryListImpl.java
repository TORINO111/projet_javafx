package dette.boutique.data.repository.listImpl;

import java.util.ArrayList;
import java.util.List;

import dette.boutique.core.repository.impl.RepositoryListImpl;
import dette.boutique.data.entities.Dette;
import dette.boutique.data.repository.DetteRepository;

public class DetteRepositoryListImpl extends RepositoryListImpl<Dette> implements DetteRepository {

    public DetteRepositoryListImpl(List<Dette> data) {
        super(data);
        //TODO Auto-generated constructor stub
    }

    public DetteRepositoryListImpl() {
        // throw new UnsupportedOperationException("Not supported yet.");
        super(new ArrayList<>());
    }

    @Override
    public Dette selectById(int id) {
        return data.stream()
            .filter(dette -> dette.getId() == id)
            .findFirst()
            .orElse(null);
    }

    @Override
    public void remove(Dette element) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'remove'");
    }
}