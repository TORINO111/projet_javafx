package dette.boutique.controllers;

import java.io.IOException;

import dette.boutique.App;
import dette.boutique.core.services.EntityManagerCreator;
import dette.boutique.core.services.RepositoryFactory;
import dette.boutique.core.services.YamlService;
import dette.boutique.core.services.impl.YamlServiceImpl;
import dette.boutique.data.entities.User;
import dette.boutique.data.repository.UserRepository;
import dette.boutique.services.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class BoutiquierController {
    public void ListeClients() throws IOException {
        App.setRoot("ListeClientsBoutiquier");
    }
    
    public void ListeDemandesDette() throws IOException {
        App.setRoot("ListeDemandesDettesBoutiquier");
    }

    public void ListeDettes() throws IOException {
        App.setRoot("ListeDettesBoutiquier");
    }

    public void FormCreerClient() throws IOException {
        App.setRoot("FormCreerClientBoutiquier");
    }
    public void Deconnexion() throws IOException {
        App.setRoot("LoginForm");
    }
}