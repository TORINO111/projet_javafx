package dette.boutique.data.repository.listImpl;

import java.util.ArrayList;
import java.util.List;

import dette.boutique.core.repository.impl.RepositoryListImpl;
import dette.boutique.data.entities.Details;
import dette.boutique.data.repository.DetailsRepository;

public class DetailsRepositoryListImpl extends RepositoryListImpl<Details> implements DetailsRepository {

    public DetailsRepositoryListImpl(List<Details> data) {
        super(data);
    }

    public DetailsRepositoryListImpl() {
        // throw new UnsupportedOperationException("Not supported yet.");
        super(new ArrayList<>());
    }

    @Override
    public void remove(Details element) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'remove'");
    }

    @Override
    public Details selectById(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'selectById'");
    }
}