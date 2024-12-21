package dette.boutique.controllers;

import java.util.List;

import dette.boutique.App;
import dette.boutique.data.entities.Role;
import dette.boutique.data.entities.Client;
import dette.boutique.data.entities.Client;
import dette.boutique.data.entities.User;
import dette.boutique.services.ClientService;
import dette.boutique.services.RoleService;
import dette.boutique.services.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.util.Callback;

public class FormCreerUserBoutiquierOuAdminController extends AdminController{
    UserService userService = App.getUserService();
    RoleService roleService = App.getRoleService();

    List<Role> roles;
    List<User> users;

    @FXML
    private ComboBox<Role> combo_choixRole;

    @FXML
    private ComboBox<Client> combo_choixClient;

    @FXML
    private TextField champ_password;
    @FXML
    private TextField champ_login;
    @FXML
    private Label label_erreur;

    @FXML
    private void initialize(){
        roles = roleService.findRoleBoutiquierEtAdmin();
        users = userService.list();

        ObservableList<Role> observableRoles = FXCollections.observableArrayList(roles);
        combo_choixRole.setItems(observableRoles);

        combo_choixRole.setCellFactory((Callback<ListView<Role>, ListCell<Role>>) new Callback<ListView<Role>, ListCell<Role>>() {
                    @Override
                    public ListCell<Role> call(ListView<Role> param) {
                        return new ListCell<>() {
                            @Override
                            protected void updateItem(Role item, boolean empty) {
                                super.updateItem(item, empty);
                                if (item == null || empty) {
                                    setText(null);
                                } else {
                                    setText( item.getNom());
                                }
                            }
                        };
                    }
                });

        combo_choixRole.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Role item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText( item.getNom());
                }
            }
        });
    }


    @FXML
    private void validerUser() {
        // Logique pour valider l'utilisateur
        String login = champ_login.getText();
        String password = champ_password.getText();
        Role role = combo_choixRole.getValue();
        
        if (login.isEmpty() || password.isEmpty() || role == null ) {
            label_erreur.setText("Tous les champs doivent être renseignés.");
        } else if(existeDansListe(login)) {
            label_erreur.setText("Ce nom d'utilisateur existe déjà");
        } else {
            User user = new User(login, password, role);
            userService.create(user);
            label_erreur.setText("Utilisateur créé avec succès!");
        }
    }

    private boolean existeDansListe(String login) {
        return users.stream()
                .anyMatch(a -> a.getLogin().toLowerCase().equals(login.toLowerCase()));
    }
}
