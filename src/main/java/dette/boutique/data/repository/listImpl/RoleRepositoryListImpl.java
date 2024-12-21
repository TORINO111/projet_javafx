package dette.boutique.data.repository.listImpl;

import java.util.ArrayList;
import java.util.List;

import dette.boutique.core.repository.impl.RepositoryListImpl;
import dette.boutique.data.entities.Role;
import dette.boutique.data.repository.RoleRepository;

public class RoleRepositoryListImpl extends RepositoryListImpl<Role> implements RoleRepository {

    public RoleRepositoryListImpl(List<Role> data) {
        super(data);
    }

    public RoleRepositoryListImpl() {
        // throw new UnsupportedOperationException("Not supported yet.");
        super(new ArrayList<>());

    }

    @Override
    public Role selectById(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'selectById'");
    }

    @Override
    public void remove(Role element) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'remove'");
    }

    @Override
    public Role findRoleByName(String nomRole) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findRoleByName'");
    }
    
}
