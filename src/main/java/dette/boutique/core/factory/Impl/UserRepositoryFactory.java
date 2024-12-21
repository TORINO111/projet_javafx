package dette.boutique.core.factory.Impl;

import dette.boutique.core.factory.RepositoryFactory;
import dette.boutique.data.repository.UserRepository;
import dette.boutique.data.repository.jdbcMysqlImpl.UserRepositoryJdbcMysqlImpl;
import dette.boutique.data.repository.jdbcPostgreImpl.UserRepositoryJdbcPostgreImpl;
import dette.boutique.data.repository.jpaImpl.UserRepositoryJpaImpl;
import dette.boutique.data.repository.listImpl.UserRepositoryListImpl;
import jakarta.persistence.EntityManagerFactory;

public class UserRepositoryFactory implements RepositoryFactory<UserRepository> {
    protected EntityManagerFactory emf;

    public UserRepositoryFactory(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public UserRepository create(String persistence) {
        switch (persistence) {
            case "JPAMYSQL", "JPAPOSTGRES" -> {
                return new UserRepositoryJpaImpl(emf.createEntityManager());
            }
            case "JDBCPOSTGRES" -> {
                return new UserRepositoryJdbcPostgreImpl();
            }
            case "JDBCMYSQL" -> {
                return new UserRepositoryJdbcMysqlImpl();
            }
            case "LIST" -> {
                return new UserRepositoryListImpl();
            }
            default -> throw new UnsupportedOperationException("Unknown persistence type: " + persistence);
        }
    }

}
