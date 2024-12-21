package dette.boutique.controllers;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import dette.boutique.App;

public class NavbarController {
    @FXML
    private Button btn_clients;

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

}
