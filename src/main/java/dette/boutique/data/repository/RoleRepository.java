package dette.boutique.data.repository;

import dette.boutique.core.repository.Repository;
import dette.boutique.data.entities.Role;

public interface RoleRepository extends Repository<Role> {
    Role findRoleByName(String nomRole);
}
