package dette.boutique;

import java.io.IOException;
import java.net.URL;

import dette.boutique.core.services.EntityManagerCreator;
import dette.boutique.core.services.RepositoryFactory;
import dette.boutique.core.services.YamlService;
import dette.boutique.core.services.impl.YamlServiceImpl;
import dette.boutique.data.entities.Article;
import dette.boutique.data.entities.Client;
import dette.boutique.data.entities.Details;
import dette.boutique.data.entities.Dette;
import dette.boutique.data.entities.Etat;
import dette.boutique.data.entities.Role;
import dette.boutique.data.entities.User;
import dette.boutique.data.repository.ArticleRepository;
import dette.boutique.data.repository.ClientRepository;
import dette.boutique.data.repository.DetailsRepository;
import dette.boutique.data.repository.DetteRepository;
import dette.boutique.data.repository.EtatRepository;
import dette.boutique.data.repository.RoleRepository;
import dette.boutique.data.repository.UserRepository;
import dette.boutique.services.ArticleService;
import dette.boutique.services.ClientService;
import dette.boutique.services.DetailsService;
import dette.boutique.services.DetteService;
import dette.boutique.services.EtatService;
import dette.boutique.services.RoleService;
import dette.boutique.services.UserService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    // Repositories
    private static UserRepository userRepository;
    private static RoleRepository roleRepository;
    private static ClientRepository clientRepository;
    private static ArticleRepository articleRepository;
    private static DetteRepository detteRepository;
    private static EtatRepository etatRepository;
    private static DetailsRepository detailsRepository;

    // Services
    private static UserService userService;
    private static RoleService roleService;
    private static ClientService clientService;
    private static ArticleService articleService;
    private static DetteService detteService;
    private static EtatService etatService;
    private static DetailsService detailsService;
    
    private static Scene scene;
    private static Stage stage;

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        initializeRepositoriesAndServices();
        scene = new Scene(loadFXML("LoginForm"), 1280, 720);

        stage.setScene(scene);
        stage.show();
    }
    
    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        URL url = App.class.getResource("/views/" + fxml + ".fxml");
        if (url == null) {
            throw new IllegalStateException("Fichier FXML introuvable: " + fxml);
        }
        FXMLLoader fxmlLoader = new FXMLLoader(url);
        return fxmlLoader.load();
    }
    
    public void initializeRepositoriesAndServices() {
        YamlService yamlService = new YamlServiceImpl();

        // Création de l'EntityManager ou de la liste selon la persistance
        EntityManagerCreator entityManagerCreator = new EntityManagerCreator(yamlService);
        Object persistenceHandler = entityManagerCreator.createEntityManagerFactory();
        String persistence = entityManagerCreator.getPersistenceName();

        // Repositories yi
        RepositoryFactory<User> userRepositoryFactory = new RepositoryFactory<>(persistenceHandler);
        userRepository = (UserRepository) userRepositoryFactory.create(persistence, User.class);

        RepositoryFactory<Role> roleRepositoryFactory = new RepositoryFactory<>(persistenceHandler);
        roleRepository = (RoleRepository) roleRepositoryFactory.create(persistence, Role.class);

        RepositoryFactory<Client> clientRepositoryFactory = new RepositoryFactory<>(persistenceHandler);
        clientRepository = (ClientRepository) clientRepositoryFactory.create(persistence, Client.class);

        RepositoryFactory<Article> articleRepositoryFactory = new RepositoryFactory<>(persistenceHandler);
        articleRepository = (ArticleRepository) articleRepositoryFactory.create(persistence,
                Article.class);

        RepositoryFactory<Dette> detteRepositoryFactory = new RepositoryFactory<>(persistenceHandler);
        detteRepository = (DetteRepository) detteRepositoryFactory.create(persistence, Dette.class);

        RepositoryFactory<Etat> etatRepositoryFactory = new RepositoryFactory<>(persistenceHandler);
        etatRepository = (EtatRepository) etatRepositoryFactory.create(persistence, Etat.class);

        RepositoryFactory<Details> detailsRepositoryFactory = new RepositoryFactory<>(persistenceHandler);
        detailsRepository = (DetailsRepository) detailsRepositoryFactory.create(persistence,
                Details.class);

        // Services yi
        articleService = new ArticleService(articleRepository);
        userService = new UserService(userRepository);
        clientService = new ClientService(clientRepository);
        roleService = new RoleService(roleRepository);
        etatService = new EtatService(etatRepository);
        detteService = new DetteService(detteRepository, articleService, clientService);
        detailsService = new DetailsService(detailsRepository);

    }


    // Getters pour les repositories
    public static UserRepository getUserRepository() {
        return userRepository;
    }

    public static RoleRepository getRoleRepository() {
        return roleRepository;
    }

    public static ClientRepository getClientRepository() {
        return clientRepository;
    }

    public static ArticleRepository getArticleRepository() {
        return articleRepository;
    }

    public static DetteRepository getDetteRepository() {
        return detteRepository;
    }

    public static EtatRepository getEtatRepository() {
        return etatRepository;
    }

    public static DetailsRepository getDetailsRepository() {
        return detailsRepository;
    }

    // Getters pour les services
    public static UserService getUserService() {
        return userService;
    }

    public static RoleService getRoleService() {
        return roleService;
    }

    public static ClientService getClientService() {
        return clientService;
    }

    public static ArticleService getArticleService() {
        return articleService;
    }

    public static DetteService getDetteService() {
        return detteService;
    }

    public static EtatService getEtatService() {
        return etatService;
    }

    public static DetailsService getDetailsService() {
        return detailsService;
    }

    public static void main(String[] args) {
        launch();
    }
}
