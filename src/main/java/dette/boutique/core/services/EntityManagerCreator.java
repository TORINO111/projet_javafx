package dette.boutique.core.services;

import java.util.ArrayList;
import java.util.List;

import dette.boutique.core.database.impl.DataBaseMySqlImpl;
import dette.boutique.core.database.impl.DataBasePostgreImpl;
import dette.boutique.data.entities.Article;
import dette.boutique.data.entities.Client;
import dette.boutique.data.entities.Details;
import dette.boutique.data.entities.Dette;
import dette.boutique.data.entities.Etat;
import dette.boutique.data.entities.Role;
import dette.boutique.data.entities.User;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class EntityManagerCreator {
    private YamlService yamlService;
    private String persistenceName;

    DataBaseMySqlImpl dataBaseMySqlImpl;
    DataBasePostgreImpl dataBasePostgreImpl;

    private List<User> userList = new ArrayList<>();
    private List<Dette> detteList = new ArrayList<>();
    private List<Client> clientList = new ArrayList<>();
    private List<Details> detailList = new ArrayList<>();
    private List<Article> articleList = new ArrayList<>();
    private List<Role> roleList = new ArrayList<>();
    private List<Etat> etatList = new ArrayList<>();

    public EntityManagerCreator(YamlService yamlService) {
        this.yamlService = yamlService;
    }

    public Object createEntityManagerFactory() {
        var yamlData = yamlService.loadYaml();
        persistenceName = (String) yamlData.get("persistence");
    
        switch (persistenceName) {
            case "LIST" -> {
                System.out.println("Utilisation des listes en mémoire.");
                return this;
            }
    
            case "JDBCPOSTGRES" -> {
                System.out.println("Utilisation de JDBC pour PostgreSQL.");
                if (dataBasePostgreImpl == null) {
                    dataBasePostgreImpl = new DataBasePostgreImpl<>() ;
                }
                return dataBasePostgreImpl.connexion();
            }
    
            case "JDBCMYSQL" -> {
                System.out.println("Utilisation de JDBC pour MySQL.");
                return dataBaseMySqlImpl.connexion();
            }
    
            default -> {
                System.out.println("Utilisation de JPA pour l'unité de persistance : " + persistenceName);

                EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceName);
                return emf;
            }
        }
    }
    

    public String getPersistenceName() {
        return persistenceName;
    }

    public List<User> getUserList() {
        return userList;
    }

    public List<Dette> getDetteList() {
        return detteList;
    }

    public List<Client> getClientList() {
        return clientList;
    }

    public List<Details> getDetailList() {
        return detailList;
    }

    public List<Article> getArticleList() {
        return articleList;
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    public List<Etat> getEtatList() {
        return etatList;
    }
}