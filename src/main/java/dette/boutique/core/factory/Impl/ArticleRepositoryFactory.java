package dette.boutique.core.factory.Impl;

import dette.boutique.core.factory.RepositoryFactory;
import dette.boutique.data.repository.ArticleRepository;
import dette.boutique.data.repository.jdbcMysqlImpl.ArticleRepositoryJdbcMysqlImpl;
import dette.boutique.data.repository.jdbcPostgreImpl.ArticleRepositoryJdbcPostgreImpl;
import dette.boutique.data.repository.jpaImpl.ArticleRepositoryJpaImpl;
import dette.boutique.data.repository.listImpl.ArticleRepositoryListImpl;
import jakarta.persistence.EntityManagerFactory;

public class ArticleRepositoryFactory implements RepositoryFactory<ArticleRepository> {
    protected EntityManagerFactory emf;

    public ArticleRepositoryFactory(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public ArticleRepository create(String persistence) {
        switch (persistence) {
            case "JPAMYSQL", "JPAPOSTGRES" -> {
                return new ArticleRepositoryJpaImpl(emf.createEntityManager());
            }
            case "JDBCMYSQL" -> {
                return new ArticleRepositoryJdbcMysqlImpl();
            }
            case "JDBCPOSTGRES" -> {
                return new ArticleRepositoryJdbcPostgreImpl();
            }
            case "LIST" -> {
                return new ArticleRepositoryListImpl();
            }
            default -> throw new UnsupportedOperationException("Unknown persistence type: " + persistence);
        }
    }
}
