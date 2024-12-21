package dette.boutique.data.repository.jpaImpl;

import dette.boutique.core.repository.impl.RepositoryJpaImpl;
import dette.boutique.data.entities.Role;
import dette.boutique.data.repository.RoleRepository;
import jakarta.persistence.EntityManager;

public class RoleRepositoryJpaImpl extends RepositoryJpaImpl<Role> implements RoleRepository {
    
    public RoleRepositoryJpaImpl(EntityManager em) {
        super(Role.class, em);
    }

    @Override
    public Role findRoleByName(String nomRole) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findRoleByName'");
    }

}
