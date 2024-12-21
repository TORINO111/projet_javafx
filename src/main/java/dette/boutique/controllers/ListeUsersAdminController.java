package dette.boutique.controllers;

import java.time.format.DateTimeFormatter;
import java.util.List;

import dette.boutique.App;
import dette.boutique.data.entities.User;
import dette.boutique.services.UserService;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;

public class ListeUsersAdminController extends AdminController{

    private final UserService userService = App.getUserService();

    private List<User> listUsers;

    @FXML
    private TreeTableView<User> champ_treeTableView;

    @FXML
    private TreeTableColumn<User, String> champ_login;

    @FXML
    private TreeTableColumn<User, Integer> champ_numero;

    @FXML
    private TreeTableColumn<User, String> champ_role;

    @FXML
    private TreeTableColumn<User, String> champ_etat;

    @FXML
    private TreeTableColumn<User, String> champ_clientAffilie;

    @FXML
    private TreeTableColumn<User, String> champ_date;


    @FXML
    public void initialize() {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            // Charger la liste des utilisateurs
            listUsers = userService.list();
            
            // Initialiser les colonnes
            champ_login.setCellValueFactory(
                    param -> new ReadOnlyObjectWrapper<>(param.getValue().getValue().getLogin()));
            champ_numero.setCellValueFactory(
                        param -> new ReadOnlyObjectWrapper<>(param.getValue().getValue().getId()));
            champ_role.setCellValueFactory(
                    param -> new ReadOnlyObjectWrapper<>(param.getValue().getValue().getRole().getNom()));
            champ_etat.setCellValueFactory(
                    param -> new ReadOnlyObjectWrapper<>(param.getValue().getValue().isActive() ? "Actif" : "Inactif"));
                    champ_clientAffilie.setCellValueFactory(param -> {
                        User user = param.getValue().getValue();
                        if (user.getClient() != null) {
                            String prenom = user.getClient().getPrenom();
                            String nom = user.getClient().getNom();
                            return new ReadOnlyObjectWrapper<>(prenom + " " + nom);
                        } else {
                            return new ReadOnlyObjectWrapper<>("Non affilié");
                        }
                    });
            champ_date.setCellValueFactory(
                    param -> new ReadOnlyObjectWrapper<>(param.getValue().getValue().getCreatedAt().format(formatter)));
            populateTreeTable();
            

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populateTreeTable() {
        TreeItem<User> rootItem = new TreeItem<>(null);
        rootItem.setExpanded(true);

        for (User user : listUsers) {
            TreeItem<User> userItem = new TreeItem<>(user);
            rootItem.getChildren().add(userItem);
        }

        champ_treeTableView.setRoot(rootItem);
        champ_treeTableView.setShowRoot(false);
    }

    
}
