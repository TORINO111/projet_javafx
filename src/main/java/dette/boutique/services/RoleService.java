package dette.boutique.services;

import java.util.List;

import dette.boutique.core.Item;
import dette.boutique.core.Service;
import dette.boutique.data.entities.Dette;
import dette.boutique.data.entities.Role;
import dette.boutique.data.repository.RoleRepository;

public class RoleService extends Service<Role> implements Item<Role> {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void create(Role element) {
        roleRepository.insert(element);
    }

    @Override
    public List<Role> list() {
        return roleRepository.selectAll();
    }



    @Override
    public void update(Role element) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    public Role findRoleByName(String name){
        return list().stream()
            .filter(role -> role.getNom().compareTo(name) ==0)
            .findFirst()
            .orElse(null);
    }

    public List<Role> findRoleBoutiquierEtAdmin(){
        return list().stream()
            .filter(role -> role.getNom().compareTo("admin") ==0 || role.getNom().compareTo("boutiquier") ==0)
            .toList();
    }


}
