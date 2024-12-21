package dette.boutique.controllers;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import dette.boutique.App;

public class DettesController {

    private static Scene scene;

    @FXML
    private Button btn_clients;

    @FXML
    private Button btn_dettes;

    @FXML
    private Button btn_users;

    @FXML
    private Button btn_Roles;

    @FXML
    private Button btn_deconnexion;

    // @FXML
    // private Button btn_dettes;

    // @FXML
    // private Button btn_users;

    // @FXML
    // private Button btn_Roles;

    // * FXML Loader à rechercher*/

    @FXML
    private void pageClients() throws IOException {
        App.setRoot("views/ClientsListe");
    }

    @FXML
    private void pageDettes() throws IOException {
        App.setRoot("views/ClientsListe");
    }

    @FXML
    private void pageUsers() throws IOException {
        App.setRoot("views/ClientsListe");
    }

    @FXML
    private void pageRoles() throws IOException {
        App.setRoot("views/ClientsListe");
    }

    @FXML
    private void deconnexion() throws IOException {
        App.setRoot("views/ClientsListe");
    }

}
