package dette.boutique.core.factory.Impl;

import dette.boutique.core.factory.RepositoryFactory;
import dette.boutique.data.repository.DetteRepository;
import dette.boutique.data.repository.UserRepository;
import dette.boutique.data.repository.jdbcMysqlImpl.DetteRepositoryJdbcMysqlImpl;
import dette.boutique.data.repository.jdbcPostgreImpl.DetteRepositoryJdbcPostgreImpl;
import dette.boutique.data.repository.jpaImpl.DetteRepositoryJpaImpl;
import dette.boutique.data.repository.listImpl.DetteRepositoryListImpl;
import jakarta.persistence.EntityManagerFactory;

public class DetteRepositoryFactory implements RepositoryFactory<DetteRepository> {
    protected EntityManagerFactory emf;
    protected UserRepository userRepository;

    public DetteRepositoryFactory(EntityManagerFactory emf, UserRepository userRepository) {
        this.emf = emf;
        this.userRepository = userRepository;
    }

    public DetteRepositoryFactory(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public DetteRepository create(String persistence) {
        switch (persistence) {
            case "JPAMYSQL", "JPAPOSTGRES" -> {
                return new DetteRepositoryJpaImpl(emf.createEntityManager());
            }
            case "JDBCPOSTGRES" -> {
                return new DetteRepositoryJdbcPostgreImpl();
            }
            case "JDBCMYSQL" -> {
                return new DetteRepositoryJdbcMysqlImpl();
            }
            case "LIST" -> {
                return new DetteRepositoryListImpl();
            }
            default -> throw new UnsupportedOperationException("Unknown persistence type: " + persistence);
        }
    }
}
