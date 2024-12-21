package dette.boutique.data.repository.jpaImpl;

import dette.boutique.core.repository.impl.RepositoryJpaImpl;
import dette.boutique.data.entities.Etat;
import dette.boutique.data.repository.EtatRepository;
import jakarta.persistence.EntityManager;

public class EtatRepositoryJpaImpl extends RepositoryJpaImpl<Etat> implements EtatRepository {
    
    public EtatRepositoryJpaImpl(EntityManager em) {
        super(Etat.class, em);
    }


}
