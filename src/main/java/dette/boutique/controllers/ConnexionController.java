package dette.boutique.controllers;

import java.io.IOException;

import dette.boutique.App;
import dette.boutique.data.entities.User;
import dette.boutique.services.UserService;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class ConnexionController {

    private static User userConnected;

    @FXML
    private TextField champ_login;

    @FXML

    private PasswordField champ_password;

    @FXML
    private Label label_erreur;

    private UserService userService = App.getUserService();

    public ConnexionController() {
        this.userService = userService;
    }

    public static User getUserConnected() {
        return userConnected;
    }

    @FXML
    private void validerConnexion() throws IOException {
        String login = champ_login.getText();
        String password = champ_password.getText();

        // Vérification des champs vides
        if (login.isEmpty() || password.isEmpty()) {
            label_erreur.setText("** Veuillez remplir tous les champs **");
            return;
        }

        // Vérification des informations utilisateur
        userConnected = userService.findUserByLoginAndPassword(login, password);

        if (userConnected != null) {
            label_erreur.setText(userConnected.getRole().getNom());
            // label_erreur.setText(userExists.getRole().getNom());
            try {
                switchUser(userConnected);
            } catch (IOException e) {
                e.getMessage();
            }
        } else {
            label_erreur.setText("Nom d'utilisateur ou mot de passe incorrect. Essayez à nouveau.");
        }

    }

    public void switchUser(User user) throws IOException {
        String role = user.getRole().getNom();
        System.out.println("Rôle de l'utilisateur : " + role); // Débogage du rôle
        switch (role) {
            case "admin" -> {
                System.out.println("Redirection vers AcceuilAdmin");
                try {
                    App.setRoot("ListeUsersAdmin");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            case "client" -> {
                System.out.println("Redirection vers AcceuilClient");
                try {
                    App.setRoot("ListeDettesClient");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            case "boutiquier" -> {
                System.out.println("Redirection vers AcceuilBoutiquier");
                try {
                    App.setRoot("ListeClientsBoutiquier");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            default -> {
                System.out.println("Rôle non reconnu");
            }
        }
    }

}
