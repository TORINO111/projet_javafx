package dette.boutique.data.repository.listImpl;

import java.util.ArrayList;
import java.util.List;

import dette.boutique.core.repository.impl.RepositoryListImpl;
import dette.boutique.data.entities.Etat;
import dette.boutique.data.repository.EtatRepository;

public class EtatRepositoryListImpl extends RepositoryListImpl<Etat> implements EtatRepository {

    public EtatRepositoryListImpl(List<Etat> data) {
        super(data);
        // TODO Auto-generated constructor stub
    }

    public EtatRepositoryListImpl() {
        // throw new UnsupportedOperationException("Not supported yet.");
        super(new ArrayList<>());
    }

}