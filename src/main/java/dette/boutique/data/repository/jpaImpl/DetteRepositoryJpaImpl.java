package dette.boutique.data.repository.jpaImpl;

import dette.boutique.core.repository.impl.RepositoryJpaImpl;
import dette.boutique.data.entities.Dette;
import dette.boutique.data.repository.DetteRepository;
import jakarta.persistence.EntityManager;

public class DetteRepositoryJpaImpl extends RepositoryJpaImpl<Dette> implements DetteRepository {

    public DetteRepositoryJpaImpl(EntityManager em) {
        super(Dette.class, em);
    }

}
