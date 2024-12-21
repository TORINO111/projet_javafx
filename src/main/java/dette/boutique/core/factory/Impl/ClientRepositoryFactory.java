package dette.boutique.core.factory.Impl;

import dette.boutique.core.factory.RepositoryFactory;
import dette.boutique.data.repository.ClientRepository;
import dette.boutique.data.repository.RoleRepository;
import dette.boutique.data.repository.jdbcMysqlImpl.ClientRepositoryJdbcMysqlImpl;
import dette.boutique.data.repository.jdbcPostgreImpl.ClientRepositoryJdbcPostgreImpl;
import dette.boutique.data.repository.jpaImpl.ClientRepositoryJpaImpl;
import dette.boutique.data.repository.listImpl.ClientRepositoryListImpl;
import jakarta.persistence.EntityManagerFactory;

public class ClientRepositoryFactory implements RepositoryFactory<ClientRepository> {
    protected EntityManagerFactory emf;
    protected  RoleRepository roleRepository;

    public ClientRepositoryFactory(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public ClientRepository create(String persistence) {
        switch (persistence) {
            case "JPAMYSQL", "JPAPOSTGRES" -> {
                return new ClientRepositoryJpaImpl(emf.createEntityManager());
            }
            case "JDBCPOSTGRES" -> {
                return new ClientRepositoryJdbcPostgreImpl();
            }
            case "JDBCMYSQL" -> {
                return new ClientRepositoryJdbcMysqlImpl();
            }
            case "LIST" -> {
                return new ClientRepositoryListImpl();
            }
            default -> throw new UnsupportedOperationException("Unknown persistence type: " + persistence);
        }
    }

}
