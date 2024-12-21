package dette.boutique.core.services;

import java.util.List;
import java.util.Map;

import dette.boutique.core.database.impl.DataBaseMySqlImpl;
import dette.boutique.core.database.impl.DataBasePostgreImpl;
import dette.boutique.core.repository.Repository;
import dette.boutique.core.repository.impl.RepositoryDbPostgreImpl;
import dette.boutique.core.repository.impl.RepositoryJpaImpl;
import dette.boutique.data.entities.Article;
import dette.boutique.data.entities.Client;
import dette.boutique.data.entities.Details;
import dette.boutique.data.entities.Dette;
import dette.boutique.data.entities.Etat;
import dette.boutique.data.entities.Role;
import dette.boutique.data.entities.User;
import dette.boutique.data.repository.jdbcPostgreImpl.ArticleRepositoryJdbcPostgreImpl;
import dette.boutique.data.repository.jdbcPostgreImpl.ClientRepositoryJdbcPostgreImpl;
import dette.boutique.data.repository.jdbcPostgreImpl.DetailsRepositoryJdbcPostgreImpl;
import dette.boutique.data.repository.jdbcPostgreImpl.DetteRepositoryJdbcPostgreImpl;
import dette.boutique.data.repository.jdbcPostgreImpl.EtatRepositoryJdbcPostgreImpl;
import dette.boutique.data.repository.jdbcPostgreImpl.RoleRepositoryJdbcPostgreImpl;
import dette.boutique.data.repository.jdbcPostgreImpl.UserRepositoryJdbcPostgreImpl;
import dette.boutique.data.repository.jpaImpl.ArticleRepositoryJpaImpl;
import dette.boutique.data.repository.jpaImpl.ClientRepositoryJpaImpl;
import dette.boutique.data.repository.jpaImpl.DetailsRepositoryJpaImpl;
import dette.boutique.data.repository.jpaImpl.DetteRepositoryJpaImpl;
import dette.boutique.data.repository.jpaImpl.EtatRepositoryJpaImpl;
import dette.boutique.data.repository.jpaImpl.RoleRepositoryJpaImpl;
import dette.boutique.data.repository.jpaImpl.UserRepositoryJpaImpl;
import dette.boutique.data.repository.listImpl.ArticleRepositoryListImpl;
import dette.boutique.data.repository.listImpl.ClientRepositoryListImpl;
import dette.boutique.data.repository.listImpl.DetailsRepositoryListImpl;
import dette.boutique.data.repository.listImpl.DetteRepositoryListImpl;
import dette.boutique.data.repository.listImpl.EtatRepositoryListImpl;
import dette.boutique.data.repository.listImpl.RoleRepositoryListImpl;
import dette.boutique.data.repository.listImpl.UserRepositoryListImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class RepositoryFactory<T> {
    private Object persistenceHandler;

    DataBaseMySqlImpl dataBaseMySqlImpl;
    DataBasePostgreImpl dataBasePostgreImpl;

    public RepositoryFactory(Object persistenceHandler) {
        this.persistenceHandler = persistenceHandler;
    }

    @SuppressWarnings("unchecked")
    public <T> Repository<T> create(String persistenceType, Class<T> entityClass) {
        switch (persistenceType) {
            case "LIST" -> {
                if (persistenceHandler instanceof EntityManagerCreator creator) {
                    Map<Class<?>, List<?>> entityLists = Map.of(
                            User.class, creator.getUserList(),
                            Dette.class, creator.getDetteList(),
                            Client.class, creator.getClientList(),
                            Details.class, creator.getDetailList(),
                            Article.class, creator.getArticleList(),
                            Role.class, creator.getRoleList(),
                            Etat.class, creator.getEtatList());

                    List<T> entityList = (List<T>) entityLists.get(entityClass);
                    if (entityList != null) {
                        return createRepository(entityClass, entityList);
                    }
                }
            }

            case "JDBCPOSTGRES", "JDBCMYSQL"-> {
                Repository<T> repository = switch (entityClass.getSimpleName()) {
                    case "User" -> (Repository<T>) new UserRepositoryJdbcPostgreImpl();
                    case "Dette" -> (Repository<T>) new DetteRepositoryJdbcPostgreImpl();
                    case "Client" -> (Repository<T>) new ClientRepositoryJdbcPostgreImpl();
                    case "Details" -> (Repository<T>) new DetailsRepositoryJdbcPostgreImpl();
                    case "Article" -> (Repository<T>) new ArticleRepositoryJdbcPostgreImpl();
                    case "Role" -> (Repository<T>) new RoleRepositoryJdbcPostgreImpl();
                    case "Etat" -> (Repository<T>) new EtatRepositoryJdbcPostgreImpl();
                    default -> (Repository<T>) new RepositoryDbPostgreImpl<>();
                };
                return repository;
            }


            case "JPAPOSTGRES", "JPAMYSQL" -> {
                EntityManagerFactory emf = (EntityManagerFactory) persistenceHandler;
                EntityManager em = emf.createEntityManager();

                Repository<T> repository = switch (entityClass.getSimpleName()) {
                    case "User" -> (Repository<T>) new UserRepositoryJpaImpl(em);
                    case "Dette" -> (Repository<T>) new DetteRepositoryJpaImpl(em);
                    case "Client" -> (Repository<T>) new ClientRepositoryJpaImpl(em);
                    case "Details" -> (Repository<T>) new DetailsRepositoryJpaImpl(em);
                    case "Article" -> (Repository<T>) new ArticleRepositoryJpaImpl(em);
                    case "Role" -> (Repository<T>) new RoleRepositoryJpaImpl(em);
                    case "Etat" -> (Repository<T>) new EtatRepositoryJpaImpl(em);
                    default -> (Repository<T>) new RepositoryJpaImpl<>(entityClass, em);
                };
                return repository;
            }

            default -> {
                throw new IllegalArgumentException("Aucun dépôt trouvé pour la classe : " + entityClass.getName());
            }
        }

        throw new IllegalStateException(
                "Aucun repository n'a été créé pour le type de persistance : " + persistenceType);
    }

    @SuppressWarnings("unchecked")
    private <T> Repository<T> createRepository(Class<T> entityClass, List<T> entityList) {
        if (entityClass == User.class) {
            return (Repository<T>) new UserRepositoryListImpl((List<User>) entityList);
        } else if (entityClass == Dette.class) {
            return (Repository<T>) new DetteRepositoryListImpl((List<Dette>) entityList);
        } else if (entityClass == Client.class) {
            return (Repository<T>) new ClientRepositoryListImpl((List<Client>) entityList);
        } else if (entityClass == Details.class) {
            return (Repository<T>) new DetailsRepositoryListImpl((List<Details>) entityList);
        } else if (entityClass == Article.class) {
            return (Repository<T>) new ArticleRepositoryListImpl((List<Article>) entityList);
        } else if (entityClass == Role.class) {
            return (Repository<T>) new RoleRepositoryListImpl((List<Role>) entityList);
        } else if (entityClass == Etat.class) {
            return (Repository<T>) new EtatRepositoryListImpl((List<Etat>) entityList);
        }

        throw new IllegalArgumentException("Aucun dépôt trouvé pour la classe : " + entityClass.getName());
    }

}
