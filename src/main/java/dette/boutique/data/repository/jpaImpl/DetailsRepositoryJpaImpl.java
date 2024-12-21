package dette.boutique.data.repository.jpaImpl;

import dette.boutique.core.repository.impl.RepositoryJpaImpl;
import dette.boutique.data.entities.Details;
import dette.boutique.data.repository.DetailsRepository;
import jakarta.persistence.EntityManager;

public class DetailsRepositoryJpaImpl extends RepositoryJpaImpl<Details> implements DetailsRepository {

    public DetailsRepositoryJpaImpl(EntityManager em) {
        super(Details.class, em);
    }

}
